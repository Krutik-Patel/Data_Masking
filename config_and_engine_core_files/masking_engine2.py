import xml.etree.ElementTree as ET
import json
import re
import logging
import argparse
from typing import Dict, Any, Union, List
from data_masking_strats import DataMaskingStrategy


class JSONDataMasker:
    def __init__(self, config_path: str):
        self.logger = logging.getLogger(__name__)
        self.config = self._load_config(config_path)
        self.patterns = self._load_patterns()
        self.strategy_map = {
            'DATE_SHIFT': DataMaskingStrategy.date_shift,
            'FIXED_LENGTH_MASK': DataMaskingStrategy.fixed_length_mask,
            'COORDINATE_SHIFT': DataMaskingStrategy.coordinate_shift,
            'IP_MASK': DataMaskingStrategy.ip_mask,
            'HASH': DataMaskingStrategy.hash_value,
            'RANDOM_NAME': DataMaskingStrategy.random_name,
            'INTEGER_HASH': DataMaskingStrategy.integer_hash
        }
        self.user_strategies = {}

    def _load_config(self, config_path: str) -> ET.ElementTree:
        try:
            return ET.parse(config_path)
        except Exception as e:
            self.logger.error(f"Failed to load config file: {e}")
            raise

    def _load_patterns(self) -> List[Dict]:
        patterns = []
        for pattern in self.config.findall('.//pattern'):
            regex_elem = pattern.find('regex')
            if regex_elem is not None:
                patterns.append({
                    'name': pattern.find('name').text if pattern.find('name') is not None else 'unnamed',
                    'regex': re.compile(regex_elem.text),
                    'strategy': pattern.find('maskingStrategy').text if pattern.find('maskingStrategy') is not None else 'FIXED_LENGTH_MASK',
                    'parameters': self._get_pattern_parameters(pattern)
                })
        print(patterns)
        return patterns

    def _get_pattern_parameters(self, pattern: ET.Element) -> Dict:
        params = {}
        for param in pattern.findall('.//parameter'):
            name = param.find('name')
            value = param.find('value')
            if name is not None and value is not None:
                params[name.text] = value.text

        # Add direct attributes as parameters
        for child in pattern:
            if child.tag not in ['regex', 'maskingStrategy', 'name', 'parameters']:
                params[child.tag] = child.text

        return params
    
    def _detect_fields(self, data: Any, prefix: str = '') -> List[str]:
        fields = []
        
        if isinstance(data, dict):
            for key, value in data.items():
                full_key = f"{prefix}.{key}" if prefix else key
                fields.extend(self._detect_fields(value, full_key))
        
        elif isinstance(data, list):
            for index, item in enumerate(data):
                full_key = f"{prefix}[{index}]"
                fields.extend(self._detect_fields(item, full_key))
        
        else:
            fields.append(prefix)
        
        return fields


    def _ask_user_for_strategies(self, fields: List[str]) -> Dict[str, str]:
        chosen_strategies = {}
        print("\nAvailable Masking Strategies:")
        for idx, strat in enumerate(self.strategy_map.keys(), 1):
            print(f"{idx}. {strat}")

        for field in fields:
            print(f"\nChoose a masking strategy for field: '{field}'")
            choice = input("Enter the number (or press Enter to skip): ")

            if choice.isdigit() and 1 <= int(choice) <= len(self.strategy_map):
                strategy = list(self.strategy_map.keys())[int(choice) - 1]
                chosen_strategies[field] = strategy
            else:
                print("No masking strategy chosen for this field.")

        return chosen_strategies

    def mask_value(self, key, value) -> str:
        value = str(value)

        if not isinstance(value, str):
            return value

        strategy_name = self.user_strategies.get(key)
        if strategy_name:
            strategy_func = self.strategy_map.get(strategy_name)
            if strategy_func:
                try:
                    return strategy_func(value, {})
                except Exception as e:
                    self.logger.warning(
                        f"Masking failed for field {key}: {e}")
                    return value
        return value

    def mask_data(self, data: Any, parent_key='') -> Any:
        if isinstance(data, dict):
            return {k: self.mask_data(v, f"{parent_key}.{k}" if parent_key else k) for k, v in data.items()}
        elif isinstance(data, list):
            return [self.mask_data(item, f"{parent_key}[]") for item in data]
        elif isinstance(data, (str, int, float)):
            return self.mask_value(parent_key, data)
        return data


def main():
    parser = argparse.ArgumentParser(
        description='Mask sensitive data in JSON files')
    parser.add_argument('input_file', help='Input JSON file path')
    parser.add_argument('output_file', help='Output JSON file path')
    parser.add_argument('--config', default='data_masking_config.xml',
                        help='Path to XML configuration file')
    parser.add_argument('--log-level', default='INFO', help='Logging level')

    args = parser.parse_args()

    # Setup logging
    logging.basicConfig(
        level=getattr(logging, args.log_level.upper()),
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    logger = logging.getLogger(__name__)

    try:
        # Load input JSON
        with open(args.input_file, 'r') as f:
            input_data = json.load(f)

        # Initialize masker
        masker = JSONDataMasker(args.config)

        # Detect fields
        fields = masker._detect_fields(input_data)
        print("\nDetected Fields:")
        for field in fields:
            print(field)

        # Ask user for strategies
        masker.user_strategies = masker._ask_user_for_strategies(fields)

        # Mask data
        print(input_data)
        masked_data = masker.mask_data(input_data)

        # Save masked data
        with open(args.output_file, 'w') as f:
            json.dump(masked_data, f, indent=2)

        logger.info(
            f"Successfully masked data and saved to {args.output_file}")

    except Exception as e:
        logger.error(f"Error processing file: {e}")
        raise


if __name__ == "__main__":
    main()

import xml.etree.ElementTree as ET
import json
import re
import random
import hashlib
from datetime import datetime, timedelta
from typing import Dict, Any, Union, List
import argparse
from pathlib import Path
import logging
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

    def mask_value(self, value) -> str:

        value = str(value)

        if not isinstance(value, str):
            return value

        for pattern in self.patterns:
            if pattern['regex'].search(value):
                strategy_name = pattern['strategy']
                print(f'{value} : {strategy_name} \n')
                strategy_func = self.strategy_map.get(strategy_name)

                if strategy_func:
                    try:
                        return strategy_func(value, pattern['parameters'])
                    except Exception as e:
                        self.logger.warning(
                            f"Masking failed for pattern {pattern['name']}: {e}")
                        return value

        return value

    def mask_data(self, data: Any) -> Any:
        if isinstance(data, dict):
            return {k: self.mask_data(v) for k, v in data.items()}
        elif isinstance(data, list):
            return [self.mask_data(item) for item in data]
        elif isinstance(data, (str, int, float)):
            return self.mask_value(data)
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

        # Mask data
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

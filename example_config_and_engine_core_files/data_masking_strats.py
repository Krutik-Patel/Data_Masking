import xml.etree.ElementTree as ET
import random
import hashlib
from datetime import datetime, timedelta
from typing import Dict
from pathlib import Path
import re


class DataMaskingStrategy:
    @staticmethod
    def date_shift(value: str, params: Dict) -> str:
        try:
            formats = ['%Y-%m-%d', '%m/%d/%Y', '%d-%m-%Y', '%Y/%m/%d']
            date_obj = None

            for fmt in formats:
                try:
                    date_obj = datetime.strptime(value, fmt)
                    shift_days = int(params.get('shiftDays', 30))
                    shifted_date = date_obj + \
                        timedelta(days=random.randint(-shift_days, shift_days))
                    return shifted_date.strftime(fmt)
                except ValueError:
                    continue

            return value
        except Exception:
            return value

    @staticmethod
    def fixed_length_mask(value: str, params: Dict) -> str:
        preserve_last = int(params.get('preserveLastDigits', 4))
        mask_char = params.get('maskChar', 'X')
        if preserve_last > 0:
            return mask_char * (len(value) - preserve_last) + value[-preserve_last:]
        return mask_char * len(value)

    @staticmethod
    def coordinate_shift(value: str, params: Dict) -> str:
        try:
            coord = float(value)
            shift_range = float(params.get('shiftRange', 0.01))
            shift = random.uniform(-shift_range, shift_range)
            return f"{coord + shift:.6f}"
        except ValueError:
            return value

    @staticmethod
    def ip_mask(value: str, params: Dict) -> str:
        octets = value.split('.')
        preserve_subnet = params.get('preserveSubnet', True)
        if preserve_subnet and len(octets) == 4:
            return f"{octets[0]}.{octets[1]}.XXX.XXX"
        return "XXX.XXX.XXX.XXX"

    @staticmethod
    def hash_value(value: str, params: Dict) -> str:
        salt = params.get('salt', '')
        return hashlib.sha256((value + salt).encode()).hexdigest()[:16]

    @staticmethod
    def random_name(value: str, params: Dict) -> str:
        preserve_length = params.get('preserveLength', True)
        if preserve_length:
            return ''.join('X' if c.isalpha() else c for c in value)
        return "big dictionary for selecting names"


    @staticmethod
    def integer_hash(value: str, params: dict) -> str:
        value = int(value)

        preserve_length = params.get('preserveLength', True)
        salt = params.get('salt', 'default_salt')

        value_str = str(value) + salt

        hash_hex = hashlib.sha256(value_str.encode()).hexdigest()

        digits = re.findall(r'\d', hash_hex)
        digit_str = ''.join(digits)

        while len(digit_str) < len(str(abs(value))):
            hash_hex = hashlib.sha256((hash_hex + salt).encode()).hexdigest()
            digits = re.findall(r'\d', hash_hex)
            digit_str += ''.join(digits)

        if preserve_length:
            length = len(str(abs(value)))  # Preserve the length of the original number
            hash_number = digit_str[:length]
        else:
            hash_number = digit_str
        
        if value < 0:
            hash_number = '-' + hash_number

        return hash_number


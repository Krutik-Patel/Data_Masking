Collecting workspace informationHere is a README for the example_config_and_engine_core_files folder:

```markdown
# Example Config and Engine Core Files

This folder contains the data masking strategies, sample data, and the core masking engine scripts. The main XML configuration file used in this project is `config_files/config_schema_v1.xml`.

## Folder Structure

- `config_files/`: Contains XML configuration files.
  - `config_schema_v1.xml`: The primary XML configuration file used for defining field information, data types, primary keys, and morphing methods.

- `data_files/`: Contains sample data files used for testing the masking engine.

- `morphed_data_files/`: Contains the output data files after applying the masking strategies.

- `data_masking_strats.py`: Python script defining various data masking strategies.

- `data_out.json`: Sample output data in JSON format.

- `data2.json`: Another sample output data in JSON format.

- `masking_engine.py`: Core masking engine script that applies the defined masking strategies to the input data.

- `masking_engine2.py`: Another version of the core masking engine script.

- `non_rel_masking_config.xml`: An additional XML configuration file for non-relational data masking.

## Important Files

### `config_files/config_schema_v1.xml`

This XML file is crucial for the project as it defines the schema for the fields, including their data types, primary key status, and morphing methods. Below is a snapshot of the file:

```xml
<!-- The primary key and foreign key infos are embedded in the fields info -->

<fields_info>
    <field>
        <field_name>field name</field_name>
        <!-- <path></path>   This is optional. Either we expect the user to give this to us. Or we can extract the xpath using the field_name -->
        <field_xPath>field xpath</field_xPath>
        <dataType>string</dataType>
        <isPrimaryKey>false</isPrimaryKey>   <!-- For primary key-->
        <refersTo></refersTo>           <!-- If the field is acting as foreign key-->
        <morphing_methods>
            <morphing_method>
                <method_name>HASH</method_name>
                <method_parameters>
                    <parameter name="algorithm">SHA256</parameter>
                </method_parameters>
            </morphing_method>
        </morphing_methods>
    </field>
</fields_info>
```

## Scripts

### `data_masking_strats.py`

This script defines various data masking strategies that can be applied to the input data. It includes methods for hashing, encryption, and other data transformation techniques.

### `masking_engine.py`

This is the core masking engine script that reads the input data, applies the defined masking strategies, and generates the masked output data.

### `masking_engine2.py`

An alternative version of the core masking engine script with additional or modified functionalities.

## Sample Data

### `data_out.json`

A sample output data file in JSON format, showing the result of applying the masking strategies to the input data.

### `data2.json`

Another sample output data file in JSON format.

## Additional Configuration

### `non_rel_masking_config.xml`

An additional XML configuration file for non-relational data masking, providing schema definitions and masking strategies for non-relational data sources.

---

This folder is essential for defining and testing the data masking strategies used in the project. The `config_schema_v1.xml` file is particularly important as it provides the schema and rules for data masking.
```
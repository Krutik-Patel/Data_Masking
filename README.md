# Backend Implementation

The backend is designed with modularity and extensibility in mind, leveraging design patterns to handle data loading, writing, and morphing efficiently. Below is an overview of the key components and their roles.

---

### Core Components

#### 1. **DataLoader**
- **Purpose**: Responsible for parsing and loading input data files (XML or JSON) into memory.
- **How it works**:
  - Reads the input file based on its format (XML or JSON).
  - Uses format-specific loaders (`XMLLoader` or `JSONLoader`) to parse the data.
  - Converts the parsed data into a unified internal representation for further processing.
- **Design Pattern**: Factory Pattern
  - The `DataLoaderFactory` determines the appropriate loader (`XMLLoader` or `JSONLoader`) based on the file type.

#### 2. **XMLLoader**
- **Purpose**: Handles the parsing of XML data files.
- **How it works**:
  - Uses an XML parser (e.g., `javax.xml.parsers.DocumentBuilder`) to read and parse the XML file.
  - Extracts data nodes based on the configuration file's XPath expressions.
  - Maps the extracted data into a structured format for processing.
- **Key Features**:
  - Supports complex XPath queries to locate specific fields.
  - Handles nested and hierarchical XML structures.
- Similarly, JsonLoader handles the json data files.

#### 3. **DataWriter**
- **Purpose**: Writes the processed (masked) data back to an output file in the desired format (XML or JSON).
- **How it works**:
  - Accepts the processed data and the desired output format.
  - Uses format-specific writers (`XMLWriter` or `JSONWriter`) to generate the output file.
- **Design Pattern**: Factory Pattern
  - The `DataWriterFactory` determines the appropriate writer (`XMLWriter` or `JSONWriter`) based on the output format.

#### 4. **XMLWriter**
- **Purpose**: Handles the generation of XML output files.
- **How it works**:
  - Constructs an XML document using the processed data.
  - Ensures that the output adheres to the original structure while incorporating the masked values.
  - Writes the XML document to the specified output file.

#### 5. **Morphers**
- **Purpose**: Apply masking strategies to the data fields as defined in the configuration file.
- **How it works**:
  - Each masking strategy (e.g., `HASH`, `REDACT`, `RANGESHIFT`) is implemented as a separate class.
  - The `MorpherFactory` selects the appropriate morpher based on the configuration.
  - Morphers are applied to the data fields during processing.
- **Design Pattern**: Strategy Pattern
  - Each masking strategy is encapsulated in its own class, allowing for easy addition of new strategies.

---

### Workflow

1. **Input Files**:
   - **Configuration File**: Defines the fields to be masked, their masking strategies, and dependencies.
   - **Data File**: Contains the raw data to be masked (XML or JSON).

2. **Loading**:
   - The `DataLoader` reads the data file and converts it into an internal representation.
   - The `XMLLoader` or `JSONLoader` is used based on the file format.

3. **Processing**:
   - The masking engine iterates over the data fields.
   - For each field, the appropriate `Morpher` is applied based on the configuration.

4. **Writing**:
   - The `DataWriter` generates the output file in the desired format.
   - The `XMLWriter` or `JSONWriter` is used based on the output format.

### Design Patterns Used

1. **Factory Pattern**:
   - Used for creating loaders (`XMLLoader`, `JSONLoader`) and writers (`XMLWriter`, `JSONWriter`).
   - Centralizes the logic for determining the appropriate class to use based on file format.

2. **Strategy Pattern**:
   - Used for implementing masking strategies (`HASH`, `REDACT`, `RANGESHIFT`, etc.).
   - Encapsulates each strategy in its own class, making it easy to add or modify strategies.

3. **Template Method Pattern**:
   - Used in loaders and writers to define the skeleton of the loading/writing process.
   - Allows subclasses to implement specific steps (e.g., parsing XML vs. JSON).

4. **Builder Pattern**:
   - Used in the `XMLWriter` and `JSONWriter` to construct the output file incrementally.

### Example Flow

1. **XMLLoader** reads an XML file and extracts data nodes based on XPath expressions.
2. The `MorpherFactory` selects the appropriate masking strategy for each field.
3. The selected `Morpher` (e.g., `HASH`) processes the field value and applies the transformation.
4. The `XMLWriter` constructs the output XML file with the masked values and writes it to disk.

This modular design ensures that the backend is both scalable and easy to extend, allowing for the addition of new file formats, masking strategies, or processing steps with minimal changes to the existing codebase.

## Morphing Methods

The backend supports a variety of morphing methods to anonymize sensitive data while preserving its utility. Each morphing method is implemented as a separate class in the `masks` folder, adhering to the **Strategy Pattern** for flexibility and extensibility. Below are the details of each morphing method:


### NONE
- **What it does**: Leaves data completely unchanged.
- **Why**: The `NONE` masking directly returns the input value without any transformation. It is done in case of non-relevant data, which needs not need hiding, or for foreign key-dependancy.

### HASH
- **What it does**: Applies a one-way cryptographic hash to the input value.
- **Why**:  
  - The `HASH` class uses cryptographic libraries (e.g., `java.security.MessageDigest`) to generate a hash.
  - In our case, due to simplicity, we have passed parameter `SHA256`, but it can be easily extended by changing the `HashMaskingStrategy.java` file.
  - It is used in pseudonymization of IDs, names, or any value that doesn’t need to be reversible.

### RANGESHIFT
- **What it does**: Adds a random offset to numeric values within a specified range.
- **Why**:  
  - The `RANGESHIFT` class generates a random shift between -10% and +10%.
  - We can optionally create a seed to make it non-randomised, and also pass shift ranges in the parameters. 
  - it is used in fields like salaries, ages, or other continuous numeric data.

### SUBSTITUTION
- **What it does**: Replaces the original value with an entry which has last four values switched to XXXX. 
- **Why**: It is used in masking credit card numbers, phone numbers, or categorical fields, by specific numbers to maintain consistency.


### REDACT
- **What it does**: Replaces the value with a fixed placeholder "REDACT".
- **Why**:  
  - The `REDACT` replaces the input value with a `placeholder` (i.e., `"REDACTED"`).
  - Free-form text fields, addresses, or sensitive notes.

### EMAIL
- **What it does**: Masks parts of an email address.
- **Implementation**:  
  - The `EmailMorpher` class splits the email into local-part and domain.
  - Masks the local-part and/or domain based on `maskLocal` and `maskDomain` parameters.
  - Optionally uses a `placeholder` for masked portions.
- **Use cases**: User emails for display in reports or UI previews.

### PARTIAL_MASKING
- **What it does**: Masks all but a variable number of characters at the start of a string.
- **Why**: The `PARTIALMASKING` masks a specified number of characters (`X`) at the `prefix`, and keeps the rest of them same. This is different from SUBSTITUTION method, as it arbitrarily masks at the prefix instead of suffix, and takes parameters which makes it utilisable for various purposes.


### RANDOMIZATION
- **What it does**: Adds random k digits or characters from k length string.
- **Why**:  This is implemented for non-deterministic morphing, for extremely sensitive data, without any seed such that there is no reversibility even by the administrator.


### BINNING
- **What it does**: Groups numeric values into bins of fixed width.
- **Why**:  
  - The `BINNING` strategy calculates bins based on the `x` parameter (bin-width exponent). This is useful for age groupings, income brackets, or coarsening continuous variables.


### NOISE_INJECTION
- **What it does**: Perturbs values by adding random noise from a specified character distribution to changed character mapping. 
- **IWhy**:  The `NOISEINJECTION` generates noise by swapping certain characters with others. This can be defined based on company requirements. It is used when masking exact names, so it cannot be used directly for searches on social medias, etc.

### K‑ANONYMIZATION
- **What it does**: Ensures each quasi-identifier combination appears in at least `k` records.
- **Implementation**:  
  - The `KAnonymizationMorpher` class suppresses or generalizes quasi-identifier values to meet the `k` threshold.
  - Here, we are considering the full data, while morphing the records.
  - Uses a suppression marker (default `"*"`) for unique values.
- **Use cases**: Demographic data requiring group anonymity.

### L‑DIVERSITY
- **What it does**: Ensures each equivalence class(quasi-identifeirs) has at least `l` distinct sensitive values.
- **Implementation**:  
  - The `LDiversityMorpher` class enforces diversity within equivalence classes by generalizing or suppressing values.
  - This helps in hiding sensitive attribute on group of data, thus, completely making the masked data irreversible.
  - Uses a suppression marker (default `"*"`) for sensitive attributes which are not L-diverse.
- **Use cases**: Health or financial data where sensitive attribute disclosure must be prevented.

### T‑CLOSENESS
- **What it does**: Limits the distance between sensitive attribute distributions in groups and the global distribution.
- **Implementation**:  
  - The `TClosenessMorpher` class calculates the distance (e.g., Earth Mover’s Distance) and adjusts values to meet the `t` threshold.
  - Uses a suppression marker (default `"*"`) for sensitive attributes which are not T-close.
- **Use cases**: Protecting skewed sensitive attributes in datasets.

### Design Patterns in Morphers
- **Strategy Pattern**: Each morphing method is encapsulated in its own class, allowing for easy addition or modification of strategies.
- **Factory Pattern**: The `MorpherFactory` dynamically selects the appropriate morpher based on the configuration file.
- **Template Method Pattern**: Common preprocessing and validation steps are defined in a base `Morpher` class, with specific transformations implemented in subclasses.

This modular design ensures that morphing methods are reusable, extensible, and easy to integrate into the masking engine.
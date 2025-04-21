## Masking Strategies

---

### NONE  
- **What it does**: Leaves data completely unchanged; useful when no masking is needed.  
- **When to use**: Testing, logging, or fields that are already public.  
- **Parameters**: _None_  
- **Example**  
  - Input: `"Alice"` → Output: `"Alice"`

---

### HASH  
- **What it does**: Applies a one‑way cryptographic hash, turning the original value into a fixed‑length digest.  
- **Use cases**: Pseudonymization of IDs, names, or any value you don’t need to reverse.  
- **Parameters**  
  - `algorithm` (e.g. `SHA256`, `MD5`)  
  - _Optional_ `salt` (random string to defend against rainbow‑table attacks)  
- **Example**  
  - Input: `"john.doe@example.com"` → Output: `"e3b0c44298fc1c14…"`

---

### RANGESHIFT  
- **What it does**: Adds a random offset to a numeric value, keeping the perturbed result within a specified min/max range.  
- **Use cases**: Salary, age, or other continuous data where you want to preserve approximate distribution but hide the exact value.  
- **Parameters**  
  - `shiftMin` (minimum shift, e.g. `-5`)  
  - `shiftMax` (maximum shift, e.g. `+5`)  
  - _Optional_ `seed` (for reproducible results)  
- **Example**  
  - Input age `30` with `shiftMin=-2`, `shiftMax=+2` → Output could be `28`, `31`, etc.

---

### SUBSTITUTION  
- **What it does**: Replaces the original value with an entry from a predefined list or mapping. Can be random or round‑robin.  
- **Use cases**: Credit card numbers, phone numbers, or categorical fields where you need realistic but fake data.  
- **Parameters**  
  - `mappingList` (in‑memory list of substitute values) **or**  
  - `mappingFile` (path to file containing allowed substitutes)  
  - `mode` (`random` vs. `roundRobin`)  
- **Example**  
  - Input debit card `4111-xxxx-xxxx-1111` → Output `5500-xxxx-xxxx-0004`

---

### REDACT  
- **What it does**: Completely removes or replaces the value with a fixed placeholder (e.g. `"REDACTED"` or empty).  
- **Use cases**: Sensitive text like free‑form notes, addresses, or any data where you want total removal.  
- **Parameters**  
  - `placeholder` (default `"REDACTED"`)  
  - _Optional_ `maskChar` (e.g. `"*"` to replace each character)  
- **Example**  
  - Input `"123 Main St."` → Output `"REDACTED"` or `"***********"`

---

### EMAIL  
- **What it does**: Masks parts of an email address, typically hiding some of the local‑part or domain.  
- **Use cases**: User emails for display in reports or UI previews.  
- **Parameters**  
  - `maskLocal` (number of characters to keep at start of local‑part, e.g. `2`)  
  - `maskDomain` (keep domain but mask subdomain, or replace domain entirely)  
  - _Optional_ `placeholder` (character to use for masked portions, default `"*"`)  
- **Example**  
  - Input `"alice@example.com"` → Output `"al***@example.com"`

---

### PARTIAL_MASKING  
- **What it does**: Keeps a fixed number of characters at the start or end, masks the rest.  
- **Use cases**: Phone numbers, SSNs, or any string where you want to reveal a prefix/suffix.  
- **Parameters**  
  - `x` (number of characters to keep)  
  - `mode` (`prefix` vs. `suffix`)  
  - `maskChar` (default `"*"`)  
- **Example**  
  - Input phone `"9876543210"` with `x=4, mode=prefix` → `"9876******"`

---

### RANDOMIZATION  
- **What it does**: Applies random noise to numeric or categorical data so exact reconstruction is difficult.  
- **Use cases**: Statistical datasets where you want to preserve trends but protect individuals.  
- **Parameters**  
  - `distribution` (`uniform`, `gaussian`, etc.)  
  - `param1`, `param2` (e.g. `mean` & `std` for Gaussian; `min` & `max` for Uniform)  
  - _Optional_ `seed`  
- **Example**  
  - Input salary `50000` with Gaussian `mean=0,std=1000` → could become `51234`, `48780`, etc.

---

### BINNING  
- **What it does**: Converts a numeric value into a bin label (range) of fixed width.  
- **Use cases**: Age groupings, income brackets, or any continuous variable you want coarsened.  
- **Parameters**  
  - `x` (bin‑width exponent: `1` → size 10, `2` → size 100)  
  - _Optional_ `inclusive` (if upper bound is inclusive, default `false`)  
- **Example**  
  - Input age `24` with `x=1` → `"20-30"`  
  - Input salary `1234` with `x=2` → `"1200-1300"`

---

### NOISE_INJECTION  
- **What it does**: Perturbs values by adding random noise drawn from a specified distribution.  
- **Use cases**: Sensor readings, GPS coordinates, or any numeric field where minor distortion is acceptable.  
- **Parameters**  
  - `distribution` (`gaussian`, `laplace`, etc.)  
  - `level` (e.g. standard deviation for Gaussian)  
  - _Optional_ `seed`  
- **Example**  
  - Input coordinate `12.9716` with Gaussian `std=0.0005` → `12.9711`, `12.9720`, etc.

---

### K‑ANONYMIZATION  
- **What it does**: Suppresses or generalizes quasi‑identifier (QI) values so each QI‑combination appears in at least **k** records.  
- **Use cases**: Demographic data where you must guarantee group anonymity.  
- **Parameters**  
  - `k` (anonymity threshold)  
  - `quasi_identifiers` (list of XPath expressions for QIs)  
  - _Optional_ `suppressionMarker` (default `"*"`)  
- **Example**  
  - QIs = [age, zip], `k=3`:  
    - Records with unique (age,zip) get age=`*`, zip=`*`

---

### L‑DIVERSITY  
- **What it does**: Builds on k‑anonymity by ensuring each equivalence class has ≥ **l** “well‑represented” distinct sensitive values.  
- **Use cases**: Health or financial data where you need to protect against sensitive attribute disclosure.  
- **Parameters**  
  - `l` (diversity threshold)  
  - `quasi_identifiers` (list of QI XPaths)  
  - `sensitive_identifiers` (list of sensitive attribute XPaths)  
- **Example**  
  - QIs = [age,zip], `l=2`, sensitive=`[disease]`:  
    - In each group, at least 2 different diseases must appear.

---

### T‑CLOSENESS  
- **What it does**: Ensures the distribution of a sensitive attribute within any QI‑group is within distance **t** of its global distribution.  
- **Use cases**: Stronger privacy guarantees for highly skewed sensitive attributes.  
- **Parameters**  
  - `t` (maximum allowed distance, e.g. Earth Mover’s Distance)  
  - `quasi_identifiers` (list of QI XPaths)  
  - `sensitive_identifiers` (list of sensitive XPaths)  
- **Example**  
  - QIs = [dept,role], `t=0.2`, sensitive=`[salary]`:  
    - Each group’s salary distribution differs by ≤ 0.2 from the company‑wide distribution.
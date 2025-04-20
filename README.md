## Added Referential Integrity Logic

### Main Steps in Implementation

1. **Maintain a `primaryKeyPath -> [foreignKeyPaths]` map**
   - This map captures the referential dependency tree. When a primary key is masked, all foreign keys that refer to it must also be updated to preserve consistency.

2. **Parse the `<refersTo>` tag** from the configuration
   - While loading the config, each field is checked for a `<refersTo>` tag to build the map from Step 1 dynamically.

3. **Recursive masking using DFS traversal**
   - When a masking operation is performed on a primary key, a depth-first traversal is triggered on all related foreign key paths, ensuring that the same transformation is applied recursively.

---

### Supporting Structures Added

#### 1. `foreignKeyMap` - Creaetd and Populated in ConfigLoader.java
- **Type**: `Map<String, List<String>>`
- Stores a list of foreign key XPaths for each primary key XPath.
- Enables recursive masking of dependent fields.

#### 2. `xPathValueToDataMap` - Created and Populated the first in DataLoader.java
- **Type**: `Map<String, Map<String, List<UnifiedHeirarchicalObject>>>`
- Used to locate data nodes by both XPath and their value.
- Crucial for identifying which fields to mask.
- After masking, this map needs to be updated using the transformation log to reflect the new values.

#### 3. `valueTransformationLog`  - Created and Maintained in ReferentialMaskingExecutor.java
- **Type**: `Map<String, String>`
- Keeps track of how original values are transformed during masking.
- Used to propagate changes across foreign key dependencies.
  
#### 4. Live remapping of `xPathValueToDataMap`
- After each masking operation, a helper method applies the `valueTransformationLog` to update the value → node mapping.
- This ensures that subsequent masking operations (especially on foreign key fields) continue to work correctly with the new values.

---

### Example Flow

1. A masking strategy is applied to a primary key field.
2. All nodes with a given `(xPath, value)` are retrieved using `xPathValueToDataMap`.
3. The value is transformed and recorded in `valueTransformationLog`.
4. The map `xPathValueToDataMap` is updated to reflect the new value.
5. All foreign key paths referring to the primary key are recursively masked using the same transformation logic.
6. This process continues in a depth-first manner across all dependencies.

---

> This approach ensures referential integrity is preserved during masking while maintaining consistent and up-to-date mappings for fast data access and transformation.



---

## THINGS LEFT TO DO

### 1. Optimize Config & Data Loading for Large Files
- **Problem**: Current implementation loads entire config and data files into memory as strings.
- **Risk**: May fail or crash for large files that don’t fit into available memory.
- **To Do**:
  - Implement streaming or chunk-based reading.
  - Consider using `BufferedReader` or JSON parsers like Jackson’s `JsonParser` with streaming mode.

### 2. Handle Cycles in Foreign Key Dependency Graph
- **Problem**: The foreign key map may contain loops or indirect cycles.
- **Risk**: Infinite recursion during masking if dependencies form a cycle.
- **To Do**:
  - Add cycle detection logic in the DFS traversal (e.g., using a visited set).
  - Alternatively, convert the graph traversal into an iterative process with a stack and processed set.
  - Ensure that each node is masked only once during the recursive traversal.

> These are important for ensuring the robustness and scalability of the masking engine in real-world datasets.

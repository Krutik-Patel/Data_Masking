import fs from 'fs';
import xml2js from 'xml2js';

// Extracts XPath-like paths from an XML structure
function getXPaths(xml, parentPath = '') {
    let xpaths = [];
    let thePaths = new Map();
    let resultPaths = {};

    function traverse(node, path) {
        if (typeof node === 'object' && node !== null) {
            Object.entries(node).forEach(([key, value]) => {
                let newPath = `${path}/${key}`;

                if (Array.isArray(value)) {
                    value.forEach(item => traverse(item, newPath));
                } else {
                    traverse(value, newPath);
                }

                // Initialize the set for the key if not already present
                // Store unique paths using Set
                if (!thePaths.has(key)) {
                    thePaths.set(key, new Set());
                }
                thePaths.get(key).add(newPath);
                xpaths.push(newPath);
            });
        }
    }

    const parser = new xml2js.Parser({ explicitArray: true });
    parser.parseString(xml, (err, result) => {
        if (err) {
            console.error("Error parsing XML:", err);
            return;
        }
        // console.log(result);
        traverse(result, '');
        thePaths.forEach((value, key) => {
            resultPaths[key] = Array.from(value);
        });
    });

    return resultPaths;
}

export default getXPaths;

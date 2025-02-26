import fs from 'fs';
import xml2js from 'xml2js';

// Extract XPaths from XML
function getXPaths(xml) {
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
        traverse(result, '');
        thePaths.forEach((value, key) => {
            resultPaths[key] = Array.from(value);
        });
    });

    return resultPaths;
}

// Get config details from XML
async function getConfigDetails(configFilePath) {
    try {
        const xmlConfigData = fs.readFileSync(configFilePath, 'utf-8');
        const parser = new xml2js.Parser({ explicitArray: false });
        const result = await parser.parseStringPromise(xmlConfigData);
        return result?.fields_info?.field || [];
    } catch (err) {
        console.error("Error parsing the config XML:", err);
        return null;
    }
}

export { getXPaths, getConfigDetails };

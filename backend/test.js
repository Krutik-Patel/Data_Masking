const fs = require('fs');
const xml2js = require('xml2js');

// Extracts XPath-like paths from an XML structure
function getXPaths(xml, parentPath = '') {
    let xpaths = [];

    function traverse(node, path) {
        if (typeof node === 'object' && node !== null) {
            Object.entries(node).forEach(([key, value]) => {
                let newPath = `${path}/${key}`;
                if (Array.isArray(value)) {
                    value.forEach((_, index) => traverse(value[index], `${newPath}[${index + 1}]`));
                } else {
                    traverse(value, newPath);
                }
                xpaths.push(newPath);
            });
        }
    }

    const parser = new xml2js.Parser({ explicitArray: false });
    parser.parseString(xml, (err, result) => {
        if (err) {
            console.error("Error parsing XML:", err);
            return;
        }
        traverse(result, '');
        console.log(xpaths);
    });

    return xpaths;
}

// Example usage
const xmlData = fs.readFileSync('./uploads/data_files/sample_data.xml', 'utf-8');
getXPaths(xmlData);

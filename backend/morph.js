const fs = require('fs');
const xml2js = require('xml2js');

// Helper Functions for Morphing
function hashValue(value, algorithm) {
    const crypto = require('crypto');
    const hash = crypto.createHash(algorithm);
    hash.update(String(value));
    return hash.digest('hex');
}

function maskValue(value, maskChar, preserveLastDigits) {
    if (typeof value !== 'string') return value;
    const preserve = parseInt(preserveLastDigits) || 0;
    if (value.length <= preserve) return value;
    return maskChar.repeat(value.length - preserve) + value.slice(-preserve);
}

function replaceValue(replacement) {
    return replacement;
}

function rangeShiftValue(value, shift, dataType) {
    if (dataType !== 'integer') return value;
    const numValue = parseFloat(value);
    return isNaN(numValue) ? value : (numValue + shift).toString();
}

function redactValue() {
    return 'REDACTED';
}

// Get Specific Morphing Function Based on Configuration
function getMorphingFunction(morphingMethod, dataType) {
    const methodName = morphingMethod.method_name;
    let params = [];
    if (morphingMethod.method_parameters && morphingMethod.method_parameters.parameter) {
        const param = morphingMethod.method_parameters.parameter;
        params = Array.isArray(param) ? param : [param];
    }
    const paramMap = {};
    params.forEach(p => {
        paramMap[p.$.name] = p._;
    });

    switch (methodName) {
        case 'HASH':
            const algorithm = paramMap['algorithm'] || 'sha256';
            return (value) => hashValue(value, algorithm);
        case 'MASK':
            const maskChar = paramMap['maskChar'] || '*';
            const preserveLastDigits = paramMap['preserveLastDigits'] || '0';
            return (value) => maskValue(value, maskChar, preserveLastDigits);
        case 'REPLACE':
            const replacement = paramMap['replacement'] || '';
            return (value) => replaceValue(replacement);
        case 'RANGE_SHIFT':
            const shift = parseInt(paramMap['shift']) || 0;
            return (value) => rangeShiftValue(value, shift, dataType);
        case 'REDACT':
            return (value) => redactValue();
        default:
            return (value) => value; // No morphing if method is unrecognized
    }
}

// Recursively Morph Fields Based on XPath
function morphFieldForXPath(pathParts, level, jsonData, morphFunction) {
    // Base case: we've reached the field to morph
    if (level === pathParts.length) {
        if (Array.isArray(jsonData)) {
            return jsonData.map(val => morphFunction(val));
        } else {
            return morphFunction(jsonData);
        }
    }
    // Prevent overflow
    if (level > pathParts.length) {
        return jsonData;
    }
    // Handle arrays (e.g., department, employee, project)
    if (Array.isArray(jsonData)) {
        jsonData.forEach((val, ind) => {
            if (val[pathParts[level]] !== undefined) {
                val[pathParts[level]] = morphFieldForXPath(pathParts, level + 1, val[pathParts[level]], morphFunction);
            }
        });
        return jsonData;
    }
    // Handle objects
    else if (jsonData[pathParts[level]] !== undefined) {
        jsonData[pathParts[level]] = morphFieldForXPath(pathParts, level + 1, jsonData[pathParts[level]], morphFunction);
        return jsonData;
    }
    // Return unchanged if path part not found
    return jsonData;
}

// Main Morphing Function
function morphData(jsonData, configDetails, xpaths) {
    configDetails.forEach(config => {
        const fieldName = config.field_name;
        const dataType = config.dataType;

        // Skip if no morphing methods are defined
        if (!config.morphing_methods || !config.morphing_methods.morphing_method) {
            console.warn(`No morphing methods defined for field: ${fieldName}`);
            return;
        }

        const morphingMethod = config.morphing_methods.morphing_method;
        const morphFunction = getMorphingFunction(morphingMethod, dataType);
        const paths = xpaths[fieldName] || [];

        paths.forEach(xpath => {
            // Convert XPath to array of parts (e.g., ['company', 'departments', 'department', ...])
            const pathParts = xpath.split('/').filter(Boolean);
            jsonData = morphFieldForXPath(pathParts, 0, jsonData, morphFunction);
        });
    });

    // Convert the morphed JSON back to XML
    const builder = new xml2js.Builder();
    const morphedXML = builder.buildObject(jsonData);
    fs.writeFileSync('./uploads/morphed_data_files/morphed_data.xml', morphedXML);
    console.log("Morphing complete. Output saved to morphed_data.xml");
}

module.exports = morphData;
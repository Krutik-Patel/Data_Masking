import React, { useState } from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";
import MaskedDataDisplay from "./MaskedDataDisplay";

function DataUploader({ onUpload, message, onMask, loading, showData, maskedData, maskedDataMessage, darkMode }) {
  const [file, setFile] = useState(null);

  // Function to determine if the string is JSON or XML
  const detectFormat = (data) => {
    try {
      // Try parsing as JSON
      JSON.parse(data);
      return "json";
    } catch (e) {
      // If JSON parsing fails, check for XML
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(data, "application/xml");
      if (xmlDoc.getElementsByTagName("parsererror").length === 0) {
        return "xml";
      }
    }
    return "unknown";
  };

  // Render the uploaded data based on its format
  const renderData = () => {
    const format = detectFormat(showData);
    if (format === "json") {
      return (
        <SyntaxHighlighter language="json" style={darkMode ? materialDark : materialLight} wrapLongLines>
          {showData}
        </SyntaxHighlighter>
      );
    } else if (format === "xml") {
      return (
        <SyntaxHighlighter language="xml" style={darkMode ? materialDark : materialLight} wrapLongLines>
          {showData}
        </SyntaxHighlighter>
      );
    } else {
      return <p>Invalid format</p>;
    }
  };

  return (
    <div>
      <h2>Upload Data File</h2>
      <input type="file" onChange={(e) => setFile(e.target.files[0])} disabled={loading} />
      <button onClick={() => onUpload(file)} disabled={loading || !file}>
        Upload Data
      </button>
      <p>{message}</p>
      {showData && (
        <div>
          <button onClick={onMask} disabled={loading}>
            Mask Data
          </button>
          <div className="side-by-side">
            <div className="xml-output">
              <h3>Sample Data</h3>
              {renderData()}
            </div>
            {maskedData && (
              <MaskedDataDisplay
                data={maskedData}
                message={maskedDataMessage}
                darkMode={darkMode}
              />
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default DataUploader;

import React, { useState } from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";

function ConfigUploader({ onUpload, message, rules, loading, darkMode }) {
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

  // Render rules based on format
  const renderRules = () => {
    const format = detectFormat(rules);
    if (format === "json") {
      return (
        <SyntaxHighlighter language="json" style={darkMode ? materialDark : materialLight} showLineNumbers wrapLongLines>
          {rules}
        </SyntaxHighlighter>
      );
    } else if (format === "xml") {
      return (
        <SyntaxHighlighter language="xml" style={darkMode ? materialDark : materialLight} showLineNumbers wrapLongLines>
          {rules}
        </SyntaxHighlighter>
      );
    } else {
      return <p>Invalid format</p>;
    }
  };

  return (
    <div className="card">
      <h2>Upload Config File</h2>
      <input type="file" onChange={(e) => setFile(e.target.files[0])} disabled={loading} />
      <button onClick={() => onUpload(file)} disabled={loading || !file}>
        Upload Config
      </button>
      <p>{message}</p>

      {rules && (
        <div className="xml-output">
          <h3>Config Rules</h3>
          {renderRules()}
        </div>
      )}
    </div>
  );
}

export default ConfigUploader;
import React from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";

function MaskedDataDisplay({ data, message, darkMode }) {
  if (!data) return null;
  
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

  const downloadData = () => {
    const format = detectFormat(data);
    const blobType = format === "json" ? "application/json" : "text/xml";
    const fileExtension = format === "json" ? "json" : "xml";
    const blob = new Blob([data], { type: blobType });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `masked_data.${fileExtension}`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
  };

  const renderData = () => {
    const format = detectFormat(data);
    if (format === "json") {
      return (
        <SyntaxHighlighter language="json" style={darkMode ? materialDark : materialLight} wrapLongLines>
          {data}
        </SyntaxHighlighter>
      );
    } else if (format === "xml") {
      return (
        <SyntaxHighlighter language="xml" style={darkMode ? materialDark : materialLight} wrapLongLines>
          {data}
        </SyntaxHighlighter>
      );
    } else {
      return <p>Invalid format</p>;
    }
  };

  return (
    <div className="xml-output">
      <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
        <h3>{message}</h3>
        <button
          onClick={downloadData}
          className="download-button"
          style={{
            background: "transparent",
            border: "none",
            cursor: "pointer",
            fontSize: "1.5rem"
          }}>
          <i className="fas fa-download"></i>
        </button>
      </div>
      {renderData()}
    </div>
  );
}

export default MaskedDataDisplay;

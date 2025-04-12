import React from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";

function MaskedDataDisplay({ data, message, darkMode }) {
  if (!data) return null;

  const downloadData = () => {
    const blob = new Blob([data], { type: "text/xml" });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "masked_data.xml";
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
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
      <SyntaxHighlighter 
        language="xml" 
        style={darkMode ? materialDark : materialLight} 
        wrapLongLines>
        {data}
      </SyntaxHighlighter>
    </div>
  );
}

export default MaskedDataDisplay;

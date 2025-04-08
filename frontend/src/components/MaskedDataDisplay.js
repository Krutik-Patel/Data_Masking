import React from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";

function MaskedDataDisplay({ data, message, darkMode }) {
  if (!data) return null;

  return (
      <div className="xml-output">
        <h2>{message}</h2>
        <SyntaxHighlighter language="xml" style={darkMode ? materialDark : materialLight} wrapLongLines>
          {data}
        </SyntaxHighlighter>
      </div>
  );
}

export default MaskedDataDisplay;

import React, { useState } from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";

function ConfigUploader({ onUpload, message, rules, loading, darkMode }) {
  const [file, setFile] = useState(null);

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
          <h3>Config Rules (XML)</h3>
          <SyntaxHighlighter language="xml" style={darkMode ? materialDark : materialLight} wrapLongLines>
            {rules}
          </SyntaxHighlighter>
        </div>
      )}
    </div>
  );
}

export default ConfigUploader;
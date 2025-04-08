import React, { useState } from "react";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { materialDark, materialLight } from "react-syntax-highlighter/dist/esm/styles/prism";
import MaskedDataDisplay from "./MaskedDataDisplay";

function DataUploader({ onUpload, message, onMask, loading, showData, maskedData, maskedDataMessage, darkMode }) {
  const [file, setFile] = useState(null);

  return (
      <div >
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
                <h3>Sample Data (XML)</h3>
                <SyntaxHighlighter language="xml" style={darkMode ? materialDark : materialLight} wrapLongLines>
                  {showData}
                </SyntaxHighlighter>
              </div>
              {
                maskedData && 
                <MaskedDataDisplay
                        data={maskedData}
                        message={maskedDataMessage}
                        darkMode={darkMode}
                />
              }
            </div>
          </div>
        )}
      </div>
  );
}

export default DataUploader;

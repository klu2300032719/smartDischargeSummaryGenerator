import React, { useState } from "react";
import { speakText, stopSpeech, getVoiceLang, playAIVoice } from "../utils/voice";

export default function SummaryView({ summary, patient, language }) {
  const [isDownloading, setIsDownloading] = useState(false);

  if (!summary) return null;

  const handleDownload = async () => {
    setIsDownloading(true);
    try {
      // ✅ changed to localhost
      const response = await fetch(`http://localhost:2819/download/${patient}`);
      if (!response.ok) throw new Error("Download failed");

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `discharge_${patient}.pdf`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);

    } catch (error) {
      alert("Error: " + error.message);
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-lg p-6 mb-8">
      <div className="flex justify-between items-center mb-4 pb-4 border-b border-slate-100">
        <h3 className="text-xl font-bold text-slate-800">Clinical Summary</h3>
        <div className="flex gap-2">
          <button onClick={() => speakText(summary, getVoiceLang(language))} className="px-4 py-2 rounded-lg bg-green-50 text-green-700">🔊 Listen</button>
          <button onClick={() => playAIVoice(summary, language)} className="px-4 py-2 rounded-lg bg-blue-50 text-blue-700">🤖 AI Voice</button>
          <button onClick={stopSpeech} className="px-4 py-2 rounded-lg bg-red-50 text-red-700">⏹ Stop</button>
          <button onClick={handleDownload} disabled={isDownloading} className="px-5 py-2.5 rounded-lg font-semibold bg-indigo-50 text-indigo-700">
            {isDownloading ? "Generating..." : "Download PDF"}
          </button>
        </div>
      </div>
      <div className="bg-slate-50/50 p-6 rounded-xl">
        <pre className="whitespace-pre-wrap font-sans text-slate-700">{summary}</pre>
      </div>
    </div>
  );
}
import React, { useState } from "react";
import { startListening } from "../utils/speech"; // single mic
import { playAIVoice } from "../utils/voice";
import { askChatbot } from "../api/api";

export default function Chatbot({ language = "en" }) {
  const [messages, setMessages] = useState([]);
  const [question, setQuestion] = useState("");

  // ⭐ Send message
  const send = async () => {
    if (!question.trim()) return;

    setMessages((prev) => [...prev, { role: "user", text: question }]);

    const res = await askChatbot(question);

    setMessages((prev) => [...prev, { role: "ai", text: res.data }]);

    // Speak AI reply
    playAIVoice(res.data, language);

    setQuestion("");
  };

  // ⭐ Mic input (fills textbox only)
  const handleMic = () => {
    startListening(setQuestion);
  };

  return (
    <div className="flex flex-col h-full">
      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {messages.map((m, i) => (
          <div
            key={i}
            className={`p-3 rounded-lg ${
              m.role === "user"
                ? "bg-indigo-100 text-indigo-800 self-end"
                : "bg-slate-100 text-slate-700 self-start"
            }`}
          >
            {m.text}
          </div>
        ))}
      </div>

      {/* Input */}
      <div className="flex gap-2 p-3 border-t">
        <input
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder="Ask something..."
          className="flex-1 px-3 py-2 border rounded-lg"
        />

        {/* Mic */}
        <button
          onClick={handleMic}
          className="px-3 py-2 bg-indigo-50 text-indigo-700 rounded-lg"
        >
          🎤
        </button>

        {/* Send */}
        <button
          onClick={send}
          className="px-4 py-2 bg-indigo-600 text-white rounded-lg"
        >
          Send
        </button>
      </div>
    </div>
  );
}
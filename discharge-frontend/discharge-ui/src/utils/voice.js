// utils/voice.js

/**
 * Standard browser-based Text-to-Speech (Fallback)
 */
export const speakText = (text, lang) => {
  const utterance = new SpeechSynthesisUtterance(text);
  utterance.lang = lang;
  window.speechSynthesis.speak(utterance);
};

export const stopSpeech = () => {
  window.speechSynthesis.cancel();
};

/**
 * Maps frontend language selection to BCP-47 codes used by APIs
 */
export const getVoiceLang = (lang) => {
  const mapping = {
    english: "en-IN",
    telugu: "te-IN",
    hindi: "hi-IN",
  };
  return mapping[lang?.toLowerCase()] || "en-IN";
};

/**
 * High-quality AI Voice using your LOCAL Spring Boot Backend
 */
export const playAIVoice = async (text, lang) => {
  try {
    const voiceLang = getVoiceLang(lang);

    // ✅ changed to localhost
    const response = await fetch(
      `http://localhost:2819/api/speak?text=${encodeURIComponent(text)}&lang=${voiceLang}`
    );

    if (!response.ok) throw new Error("AI Voice backend failed");

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const audio = new Audio(url);
    await audio.play();
  } catch (error) {
    console.error("AI Voice Error:", error);
    alert("AI Voice failed. Using browser voice instead.");
    speakText(text, getVoiceLang(lang));
  }
};

/**
 * Browser-based Speech-to-Text for the Medical Chatbot
 */
export const startListening = (setText) => {
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
  if (!SpeechRecognition) {
    alert("Speech recognition not supported");
    return;
  }
  const recognition = new SpeechRecognition();
  recognition.lang = "en-IN";
  recognition.onresult = (event) => setText(event.results[0][0].transcript);
  recognition.start();
};
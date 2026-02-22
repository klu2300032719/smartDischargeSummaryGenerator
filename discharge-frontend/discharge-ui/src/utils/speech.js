export const startListening = (setText) => {
  const SpeechRecognition =
    window.SpeechRecognition || window.webkitSpeechRecognition;

  if (!SpeechRecognition) {
    alert("Speech recognition not supported");
    return;
  }

  const recognition = new SpeechRecognition();
  recognition.lang = "en-IN";
  recognition.interimResults = false;

  recognition.onresult = (event) => {
    const text = event.results[0][0].transcript;
    setText(text);
  };

  recognition.start();
};
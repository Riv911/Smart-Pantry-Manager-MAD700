# Video quality and real demonstration guide

The original narrated **preview slideshow** is 392.5 seconds, 960 × 540 pixels, only **2 fps**, with **22.05 kHz mono AAC audio**. This is why playback feels jumpy and the voice may lack clarity. `SmartPantryManager_Preview_Enhanced.mp4` has been re-encoded at 1280 × 720 / 24 fps with mild noise reduction, a speech-level compressor and -16 LUFS loudness normalization, then H.264/AAC encoding. The enhanced 24 fps stream repeats the original frames; it **cannot restore missing frames, original recording detail, or an authentic live app demonstration**. The original voice was retained rather than substituted.

## Record the actual submission video on Windows
1. Get the app running on an emulator/phone and publish the GitHub repository first.
2. Install OBS Studio (https://obsproject.com/). Add `Window Capture` for Android Studio/emulator and `Audio Input Capture` for your microphone. Check the recording before proceeding.
3. In OBS **Settings > Video** select 1920 × 1080 Canvas/Output and 30 FPS (720p/30 is acceptable if your PC struggles, but the guideline prefers 1080p where possible).
4. In **Settings > Audio**, use 48 kHz audio. Put the microphone about 15–20 cm away, speak at a steady volume, and keep its meter approximately between -18 and -6 dB (avoid the red clipping zone). Add a Noise Suppression filter (RNNoise if available), a light Compressor and a Limiter set around -1 dB. Do not over-filter speech.
5. In **Output > Recording**, record MP4/H.264, or record MKV and use **File > Remux Recordings** to create MP4. Make a 20-second test and listen back on headphones to check clarity and sync.
6. Record a single 5–7 minute file following the breakdown below and **use your own continuous narration**, including actual app interaction, database persistence and code explanation.

## Six-minute live recording outline
- 0:00–1:00: Show the **public GitHub repository**, README and real commit history. Explain genuine changes without misrepresenting commit dates.
- 1:00–3:20: Launch app. Show Pantry, Add, error validation, Edit, Delete, List, Suggestions and Recipe Detail, and Settings. Demonstrate Omelette appearing with required ingredients, disappearing when oil is deleted, and reappearing when oil is added. Show pantry persistence after force-closing and reopening the app.
- 3:20–5:30: In Android Studio, point to three real code concepts: `SQLiteOpenHelper` CRUD/seed, `PantryAdapter` with `ListView`, and `StrictMatchEngine` unit conversion/quantity logic; optionally show `onResume` and Intents.
- 5:30–6:15: Explain SQLite: local, offline, suitable for small pantry data, no cloud server.
- 6:15–6:30: Short recap with app running.

## Keep the real recording under the 50 MB total ZIP limit
Keep **all files together** including project, report and MP4. Use HandBrake (https://handbrake.fr/) or FFmpeg if the video is too large. For a mostly static IDE/emulator screen, try:

```powershell
ffmpeg -i "MyLiveRecording.mp4" -c:v libx264 -preset slow -crf 26 -maxrate 750k -bufsize 1500k -pix_fmt yuv420p -c:a aac -b:a 96k -ar 48000 -movflags +faststart "SmartPantryManager_Live_Demonstration.mp4"
```

Preserve the original resolution and readable source-code text where practical. **Play the resulting file to check intelligibility** and confirm a final 5–7 minute duration, genuine app recording, visible GitHub, voice and code. Replace the enhanced preview video in the ZIP with this actual recording before academic submission.

# Build the APK from your phone — no PC

1. Create a free GitHub account if you don't already have one.
2. Create a **public** repository named `GanapatiAI`.
3. Upload the files/folders from this project to the repository, keeping the same folder structure.
4. Open the repository → **Actions** → **Build Ganapati AI APK** → **Run workflow**.
5. Wait for the green check.
6. Open the completed workflow run → **Artifacts** → `GanapatiAI-debug-apk`.
7. Download the ZIP, extract it, and install `app-debug.apk` on your Android phone.
8. If Android asks, allow installation from the browser/file manager you used.

GitHub states that Actions is free for standard runners in public repositories. This workflow therefore avoids a paid build service for a public repo.

For a Play Store release later, the app should use a proper signed release key rather than this debug APK.

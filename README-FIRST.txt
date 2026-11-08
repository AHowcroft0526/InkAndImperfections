INK & IMPERFECTIONS — ANDROID TEST BUILD

What this is:
A starter Android project for a first phone-test version. It currently includes:
- A journal home screen
- Create, edit, and delete journal entries
- Local on-device saving (entries stay on this phone)
- A few visual moodboard selectors to begin the aesthetic direction
- A GitHub Actions workflow that builds a debug APK in the cloud

Important:
This is an early starter build, not the finished fully customizable scrapbook app.
The current version does not yet include photo uploads, drag-and-drop stickers,
custom page layouts, cover editing, export/backup, or all ten aesthetic asset packs.

HOW TO BUILD IT USING ONLY YOUR PHONE:
1. Extract this ZIP if your phone's file manager does not show the folders inside it.
2. Open github.com in Chrome and sign in (or create a free account).
3. Create a new repository named InkAndImperfections. Choose Public or Private; either is fine.
4. On the repository page, choose Add file > Upload files.
5. Upload the CONTENTS of this project folder (not the outer ZIP). Make sure the .github folder is included.
   If GitHub's mobile page makes folder uploading difficult, use Chrome's menu > Desktop site.
6. Commit the uploaded files to the main branch.
7. Open the repository's Actions tab. Select "Build Android APK" if shown, then choose Run workflow.
   A push to main may also start the build automatically.
8. When the run finishes successfully, open it and scroll to Artifacts.
9. Download InkAndImperfections-debug-apk. GitHub may download it as a ZIP; extract it and tap app-debug.apk.
10. If Android asks, allow Chrome or Files to install unknown apps, then install the APK.

If a build fails:
Open the failed workflow run and copy the first red error section. Send it here and I can help fix it.

Privacy:
Journal entries in this starter version are stored locally in the app's private preferences on the phone.
Uninstalling the app or clearing its app data can erase them. This starter version has no cloud sync.

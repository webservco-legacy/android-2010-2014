# android-2010-2014

My Android projects of 2011 to 2015, one folder each with its own history: aPocket (my wallet app), a sandbox and a
gallery of 2011, the tutorials of android-developer.ro, an Android ID app, the WebView shell of my site (2012), a storage
test and the later WebView shell (2015). A historical archive, kept read-only: built for the Android SDK of its time
(C2DM and GCM have been shut down), not maintained.

## History

One folder per project, each with its own history (joined in 2026 by a merge commit; the counts are of this copy):

| Folder | What | Years | Commits | Branches |
|---|---|---|---|---|
| `apocket/` | aPocket, my wallet app: accounts and entries in SQLite, import and export, user accounts in 0.4; the APK of 0.4 in `extra/releases/` | 2011 - 2012 | 73 | `apocket/0.4` |
| `android-sandbox-2011/` | my sandbox of 2011: a grid of thumbnails, a 3D rotation, swipes and the back button | 2011 | 25 | - |
| `android-gallery-2011/` | a swipe gallery with a menu and an about page, from local or remote pictures | 2011 | 30 | - |
| `android-tutorials/` | the code of the android-developer.ro tutorials: C2DM, GCM, work in the background, a gallery | 2011 - 2014 | 17 | - |
| `android-id/` | an app that shows the device's Android ID | 2012 - 2014 | 7 | `android-id/working-copy-2014-08` |
| `radu-android/` | the WebView shell of my site, 2012 | 2012 | 11 | `radu-android/v1_backproblem` |
| `storage-test/` | a test of the device's storage places | 2013 | 2 | - |
| `webview-shell-s/` | the later WebView shell under my own package (2015), the forerunner of HAWA | 2015 | 4 | - |

The tags of aPocket are not kept; its versions were tagged from bfb8c04 (0.2, 2012-03-31),
79bbde0 (0.3, 2012-04-01) and 70d0c8c (0.4, 2012-04-03).

The sample photos of the gallery and the sandbox are kept as they were committed.

## How it was built

From my Subversion repositories (their dumps) and Eclipse working copies, turned into git in 2026, the dates kept; the
working copies' edits that were never committed are commits of their own. The last commit of each project before the
merge sets LF line endings and indents with spaces (not in the third-party code).

## Third-party code

The Android support library (`android-support-v4.jar`, Apache License 2.0), as the projects shipped it.

## Licence

MIT (see `LICENSE`), added in 2026: the projects named no licence.

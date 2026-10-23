# Publishing to GitHub — Windows and Android Studio

Your submitted project contains an existing local `.git` history but **no authenticated remote**. Publishing requires access to your own GitHub account. I cannot open an account or push code to your account without authenticated authorisation. Never send a password or personal access token in chat.

## A. Create or sign into a GitHub account
1. Go to https://github.com/signup to make an account, or https://github.com/login to sign in. Verify the email address when requested.
2. In GitHub click **+** (top-right) > **New repository** (or go to https://github.com/new).
3. Enter `Smart-Pantry-Manager-MAD700`, choose **Public**, and **do not** check `Add a README`, `.gitignore`, or license: this local folder already contains files/history.
4. Click **Create repository**. Keep the browser page open.

## B. Link this existing local project to the repository
Extract the final ZIP first, then open the `Smart-Pantry-Manager-MAD700` folder in PowerShell and run:

```powershell
git status
git config user.name "YOUR REAL NAME"
git config user.email "YOUR_GITHUB_EMAIL@example.com"
git add .
git commit -m "Repair strict matching validation and Gradle setup"
git remote -v
git remote add origin https://github.com/YOUR_USERNAME/Smart-Pantry-Manager-MAD700.git
git branch -M main
git push -u origin main
```

Replace `YOUR_USERNAME` and the Git configuration examples with your real GitHub details. The repaired files are deliberately left uncommitted so the next commit reflects your own acknowledgement of the repairs; the existing local history remains intact. `git remote -v` should initially show no remote. If you already added `origin`, replace `git remote add ...` with `git remote set-url origin ...`. GitHub will prompt you to authenticate; use the browser/Git Credential Manager workflow, **not a GitHub password pasted into the terminal**.

## C. Alternative: GitHub CLI
Install GitHub CLI from https://cli.github.com/ . In a terminal within this project, run:

```powershell
gh auth login
gh repo create Smart-Pantry-Manager-MAD700 --public --source=. --remote=origin --push
```

Follow the login instructions. If the repository already exists, use method B instead.

## D. Verify before submitting
Open `https://github.com/YOUR_USERNAME/Smart-Pantry-Manager-MAD700` in a private/incognito browser window. Confirm README and source are readable, and review **Commits** on the main branch. Copy the **actual** link into your report; verify it after saving the report. The local history imported with the original ZIP has a cluster of milestone commits with near-identical timestamps, so do **not** present it as proof of work across several dates. Continue committing authentic new changes as you make them; never falsify dates or commit history. Ask the lecturer if retrospective history is acceptable.

If you use Android Studio, its `Git > GitHub > Share Project on GitHub` action may be used **after** you sign in, but verify that it publishes this existing history and not a new unrelated repository.

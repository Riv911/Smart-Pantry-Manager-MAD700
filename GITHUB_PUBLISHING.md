# Publishing and updating Smart Pantry Manager on GitHub

**Student:** Rivash Prithipal  
**GitHub account:** `Riv911`  
**Repository:** https://github.com/Riv911/Smart-Pantry-Manager-MAD700  
**Git email:** `prithipalrivi@gmail.com`

The existing project was connected to GitHub from PowerShell. Keep working inside the existing local repository, which preserves its authentic development history. Do not create a second repository or reset its Git folder.

## Verify the existing connection
In PowerShell, open the folder containing `app/`, `README.md`, and `.git/`, then run:

```powershell
git config user.name "Rivash Prithipal"
git config user.email "prithipalrivi@gmail.com"
git remote set-url origin https://github.com/Riv911/Smart-Pantry-Manager-MAD700.git
git remote -v
git status
```

The fetch and push URLs should both show `Riv911/Smart-Pantry-Manager-MAD700.git`.

## Apply the revised files
The updated submission ZIP includes `UPDATE_EXISTING_GITHUB_REPO.ps1` alongside the updated Android project. Extract it to a NEW directory so your working Git folder stays safe. Open PowerShell in the newly extracted ZIP folder, then run the command below (adjust only the existing repository folder path):

```powershell
.\UPDATE_EXISTING_GITHUB_REPO.ps1 -RepositoryPath "C:\path\to\your\existing\Smart-Pantry-Manager-MAD700"
```

Replace the example repository path with the exact directory you have already connected. The script checks for uncommitted changes and preserves `.git/`.

## Record and publish the change
In your existing local repository:

```powershell
git add -A
git commit -m "Polish project documentation and video walkthrough wording"
git push origin main
```

If Git reports there is nothing to commit, compare the two directories and ensure the script was run with the correct path. Verify the result at https://github.com/Riv911/Smart-Pantry-Manager-MAD700.

Git history should be authentic: do not rewrite dates or invent past changes. If your lecturer needs evidence of development over time, present your real commits and explain your actual workflow.

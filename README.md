# Socket Bar

**This projects uses git-flow for project versioning.**

## Useful Links

  **Git flow commands: https://danielkummer.github.io/git-flow-cheatsheet/**

  **GitHub repository: https://github.com/julio-delgado/socket-bar**


## Tutorial to download and setting the project

  **1. Create a directory named <Projects>**

  **2. On Projects, create a directory named <socket-bar>**

  **3. On socket-bar directory on terminal, run the command bellow:**
    `git clone https://github.com/julio-delgado/socket-bar.git`

  **4. To install git-flow, just type on terminal:**
    `sudo apt-get install git-flow`

  **5. To initialize git flow, run the command:**
    `git flow init`

## Rules for good development

  **1. Always work on a specific branch (feature or bugfix)**

  **2. Never work directly on master!**

  **3. Write your commits following the pattern below:**
    `(type) Short and clear description of what your code updates do.`
    Where type can be:
      -(feat) for a feature;
      -(fix) for code fix.
      ex: `(feat) Implements server socket.`

## How to start coding

  **1. With the repository already cloned, git flow installed and initialized, it's time to code!**

  **2. Update develop branch, ensuring you have the current version:**
    `git pull`

  **3. From develop, create a new branch for your current task:**
    If the task is a new feature or function:
      `git flow feature start <nome_da_branch>`

    If the is a bugfix:
      `git flow bugfix start <nome_da_branch>`

  **4. After your changes, adds and commits, it's time to finish your branch**

  **5. Go back to develop**
    `git checkout develop`

  **6. Update develop**
    `git pull`

  **7. To avoid upload corrupted code, we will always resolve the conflicts in the work branch.
    So go back to your branch**
    `git checkout <nome_da_branch>`

  **8. Apply develop updates to your branch**
    `git rebase develop`

  **9. With possible conflicts resolved, your branch is safe to be merged to develop.**
    `git flow feature finish`

  **10. Now again at develop, updates again just for safety**
    `git pull`

  **11. Update the remote repository uploading your changed files:**
    `git push`

# me202app
## How to get this on your computer
1. Make an account on GitHub. Tell me what your username is so I can add you as a contributor.
2. Create an empty folder on your computer to contain the project.
3. Go to this folder in your terminal. Type `git clone https://github.com/Morgan10E/me202app.git`
4. You now have your own personal version of the project! You should be able to open and modify it in Android Studio.

## How to add your changes to the master version
1. Go to your project folder. Type `git status`. You should see your changes listed.
2. If you want to add all of these changes, type `git add -A`. `git status` should now show these changes as staged for commit.
3. `git commit -m "***"` where you replace the *** with a commit message. This typically describes what your changes were.
4. `git pull` to make sure you have the most recent version of the master version. Run your app again after doing this to make sure everything still works.
5. If everything still works, `git push` will push your changes onto the master version.

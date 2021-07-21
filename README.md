# Harmony_Amplify_Library
Amplify library for Harmony OS source code
Amplify library is used to prompt users for feedback at the right times. Based on ruled defined by us library determines the right time to ask for feedback and based on their reaction, users are given choice to leave a quick rating/review on the Huawei App gallery or send a complaint email 

![first](https://user-images.githubusercontent.com/48115293/124867426-b86e4b80-dfdb-11eb-9afc-d2637d67eadf.png)


# Source
Inspired from android library https://github.com/stkent/amplify

## Features
We can define several rules to determine the right time to prompt for feedback which takes into account the number of days since the app is installed, last asked for feedback, update in the app etc. Library tracks significant events and prompts only when all defined rules allow to do so

In addition to this we can design the layout of the feedback prompt. Users with positive feedback are given choice to leave a quick rating/review on the Huawei App gallery and those with critical feedback are given choice to send a complaint email that will automatically include pertinent app and device information

(Currently the feature to open email client from the app is not there, so for time being they are also given choice to review on App gallery)


![first](https://user-images.githubusercontent.com/48115293/124867426-b86e4b80-dfdb-11eb-9afc-d2637d67eadf.png)
![second](https://user-images.githubusercontent.com/48115293/124867576-f8cdc980-dfdb-11eb-86cc-291c2b19e125.png)


If User gives positive feedback
![image](https://user-images.githubusercontent.com/48115293/123784604-3dc77100-d8f5-11eb-8baa-10cc5855bf9f.png)


If we get critical feedback
![image](https://user-images.githubusercontent.com/48115293/123785115-c8a86b80-d8f5-11eb-982e-a613c3b0e7f8.png)




## Usage
1. Initialize the shared Amplify instance in the MyApplication class and supply feedback collectors that determine where positive and critical feedback should be directed
2. 
![image](https://user-images.githubusercontent.com/48115293/123786101-f17d3080-d8f6-11eb-8abc-ce677b6f2993.png)



By default, the HuaweiAppgalleryFeedbackCollector will search their stores for the app whose package name matches the running application. If your app's build variants do not all share a single package name, amplify will fail to load the appropriate App Gallery page in non-release builds. To fix this, pass your release build package name to the
FeedbackCollectors during initialization.


![image](https://user-images.githubusercontent.com/48115293/123785863-a2370000-d8f6-11eb-86fb-62be85cea9b3.png)


2. Add a DefaultLayoutPromptView instance to XML layouts where you may want to prompt the user for their feedback

![image](https://user-images.githubusercontent.com/48115293/123785671-70259e00-d8f6-11eb-92ff-789c6a03b1c2.png)


3. Get the shared Amplify instance in the MainAbilitySlice class and call promptIfReady method and pass the DefaultLayoutPromptView instance:
  If nothing specified, prompt will come with default settings we can customize the prompt and configure the prompt in the xml or in the code
  (Code to configure prompt is provided in MainAbilitySlice in Sample App)
![image](https://user-images.githubusercontent.com/48115293/123785921-b4b13980-d8f6-11eb-9644-d94722f2d7ec.png)


## Future Work
1. Opening email client when user gives critical feedback, currently opening app gallery for both positive and critical feedback

2. Post Delayed 

![image](https://user-images.githubusercontent.com/48115293/123788569-cb0cc480-d8f9-11eb-8e2a-79418a340f35.png)


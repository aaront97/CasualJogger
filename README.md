# CasualJogger
A weather app targetted specifically for runners, Interaction Design Group #29.

Target systems:
-Computers with Java and JavaFX installed, with Java 8 and higher. 

Running the app:
//TODO

External Libraries Used:
-org.json, a library to manipulate JSON objects, which we included as a Maven dependency. https://mvnrepository.com/artifact/org.json/json/20140107
-JavaFX, to build most of the components of our app. 

External Code Used:
-For our dark theme, we used a pre-made JavaFX theme, which we imported as a CSS file. The CSS file we downloaded has been released under the MIT license, which allows modification, distribution, as well as commercial and private use. Nevertheless, we will give credit to the GitHub user joffrey-bion, whose repository can be found at: https://github.com/joffrey-bion/javafx-themes.

Issues:
-Due to there being a limit on how many API calls are sent to the API provider each day, there might be a chance that the application will throw an error message or show unexpected results as a result of prolonged use. One important thing to note is that the air quality and pollen information provider, Breezometer, imposes a very low limit on the number of API calls (9-10 each day). 

If this happens, an error message 'API Call Limit Has been exceeded' will be shown on the weather app.
To handle this, create a new account with Breezometer and replace the Breezometer variable in the apiKeys.json file. 
This hindrance is due to the financial constraints imposed on this project. 

Sign-up Link for Breezometer: 
https://developers.breezometer.com/signup

lamo


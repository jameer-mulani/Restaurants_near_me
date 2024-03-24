# Restaurants near me
Simple MVVM Clean architecture based application shows near by restaurants using foursquare api.

**``App Version:1.0``**
**``Min SDK:24``**
**``Compile sdk:34``**
**``Target Sdk:34``**

#### Functional Features:
1. Fetch users current location.
2. Query Foursquare api and get the near by restaurants.
3. Show fetched locations in the list.
4. Clicking on restaurant item in the list navigate to the details screen.
5. Details screen shows Restaurant image, address and show in map feature.
6. Open map app and display selected location.

#### Screens(UI) Elements:
##### Screen1 (Restuarant list screen):
1. Query users current location.
2. Fetch the restaurants from FSQ.
3. Display restaurants list.
4. Handle user click gesture.
5. Navigate to Restaurant details screen.

##### Screen2 (Restaurant details screen):
1. Displays details of selected restaurant.
2. Allow user to see selected location in map application.

## Instructions to run app
1. Download or check out repo.
2. Enter your FourSquare places api key in ```app/build.gradle``` under ```defaultConfig{}```.
3. Run app on device or emulator.

## What could be done better (for future):
1. Integration of navcomponents for in app navigation.
2. Better use of ```Fragments``` to make ui more reusable and support various form factors.
3. Increased unit test code coverage.
4. Better image caching mechanism or integration of image loading libraray recommeded by google e.g. Glide, coil etc.
5. Enhanced ui and motion animation.
6. Integration of splash screen for better ux.
7. Support for restaruant image gallery on restaurant details page.

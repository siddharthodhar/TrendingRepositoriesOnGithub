# TrendingRepositoriesOnGithub
The Trending Repositories app shows you a list of trending repositories on Github as served by a public Rest API(https://githubtrendingapi.docs.apiary.io/#) provided by Github.

The source code of the app is structured using MVVM architecture. It uses RxJava for asynchronous operations and Dagger android for dependency injection. Retrofit is used for making network calls and Flowable output is converted to LiveData streams to observe easily inside an activity. The app works offline as well as the data is saved as a local cache with the help of the Room persistence library. Glide is used for loading images from URL.

There is a search filter option that can filter the list according to the repository name. The list handles item click by showing an option to open the Github repository in a browser. Pull to refresh for force fetching repositories from remote function available.

The UI is designed using Constraint Layout for better performance.

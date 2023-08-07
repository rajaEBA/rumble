# My Android App

This is a Android app that demonstrates how to display a list of items, details of item, also, profile of user.

## Features
- Login via credential
- Displays a list of items using RecyclerView in ArticleFragment
- Display article's details 
- Profile's user

## ViewModel

- ArticleViewModel
  A ViewModel class responsible for providing article data to the UI.
  It utilizes the ArticlesUseCase to fetch articles and updates the view state accordingly.
  The view state includes Loading, NoDetails, LoadingArticlesFailed, LoadDetails and ArticlesLoaded states to handle different loading scenarios.

- ArticleDetailsViewModel
  A ViewModel class responsible for handling article details related actions and state.
  It uses a SharedFlow to emit the DetailsLoaded state when the with article details loaded.

- LoginViewModel
  The LoginViewModel is a ViewModel class responsible for handling login events in the UI.
  It utilizes the LoginViaCredentialUseCase and AuthorizeTokenUseCase to perform login operations and updates the view state accordingly.
  The view state includes NoResults, FailedLogin, LunchMain states to handle different login scenarios.

## how

I used Clean Architecture with three distinct layers - infrastructure, domain, and presentation - to separate logic from the view.
Koin was employed for dependency injection, ensuring modularity, testability, and scalability of the application.

## light mode & dark mode

In resource folder in Theme folder there are 2 different file themes.xml and themes.xml(night)
Also,It could be done by colors.
For instance, you could check PrimaryButtonStyle out.(Login button in login Activity)

## question
What are your thoughts on the API responses?

Here are some specific thoughts on the API responses:

The /users/login endpoint returns a lot of user information, including the user's email address, password, first name, last name, phone number, address, and active subscriptions. 
This is a lot of information to return in a single response, and it may not be necessary for all use cases.
access_token would be enough.


The /users/me endpoint is a good way to get a user's profile information, but it would be helpful to also include the user's last login date and time in the response.
also, name of variable has been used should be correct for example : fisrtname,adress

The /Articles/:ArticleID endpoint returns information about a specific article. It would be better change to /ArticleID.

## question
If you included a framework, why did you choose this?

used a number of architectural patterns and libraries to build my application, including Clean Architecture, MVVM, StateFlow, SharedFlow, Gson, Retrofit, Coroutines, and Koin. 
I also wrote unit tests to ensure the correctness of my logic.

Here is a more detailed explanation of each of the technologies mentioned:

- Clean Architecture is a software design pattern that separates the concerns of my application into different layers. This makes my application more modular and easier to maintain.
- MVVM (Model-View-ViewModel) is a software design pattern that separates the presentation layer from the data layer. This makes my application more maintainable and easier to test.
- StateFlow and SharedFlow are reactive programming libraries that allow me to handle asynchronous data streams. This makes my application more responsive and performant.
- Gson is a JSON parser that makes it easy to parse and serialize JSON data.
- Retrofit is a REST client library that makes it easy to interact with REST APIs.
- Coroutines are a way to write asynchronous code in a more concise and readable way.
- Koin is a dependency injection library that makes it easy to manage dependencies in my application.

## question
If you would have more time, what would you have done differently?
- using jetpack compose
- using Android dependency catalog
- store token encrypted not the plain token
# MovieFinder
Movie finder app with clean architecture using the TMDB API.

## Features
- Home screen with popular, top rated and upcoming movies
- See more for all three categories using pagination
- Movie details
- Watchlist with TMDB login
- Search

## Screenshots
<p>
  <img src="https://github.com/holparb/movie-finder-android/blob/main/screenshots/01.png" width="250" />
  <img src="https://github.com/holparb/movie-finder-android/blob/main/screenshots/02.png" width="250" />
  <img src="https://github.com/holparb/movie-finder-android/blob/main/screenshots/03.png" width="250" />
  <img src="https://github.com/holparb/movie-finder-android/blob/main/screenshots/04.png" width="250" />
  <img src="https://github.com/holparb/movie-finder-android/blob/main/screenshots/05.png" width="250" />
  <img src="https://github.com/holparb/movie-finder-android/blob/main/screenshots/06.png" width="250" />
</p>

## How to run
In the local.properties file add your TMDB key:
```bash
API_ACCESS_TOKEN="youraccesskey"
```

## Libraries used
- Coil
- Room
- Ktor
- Hilt
- Paging3
- MockK
- Turbine
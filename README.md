# MyMusic

[![JUnit Test Flow](https://github.com/leleonelciandt/projetct-myMusic-toTal/actions/workflows/unit-test-workflow.yml/badge.svg)](https://github.com/gruvat/projetct-myMusic-toTal/actions/workflows/unit-test-workflow.yml)

MyMusic is an API for musics and playlists management. It was developed by the group No Exceptions
as the final project of the CI&T Java Bootcamp. It also has an integration with a Token Provider API that 
was previously created. 

## API Spec
<a href="https://mymusic-application.herokuapp.com/mymusic/swagger-ui/index.html">
<img src="https://validator.swagger.io/validator/?url=https%3A%2F%2Fmymusic-application.herokuapp.com%2Fv3%2Fapi-docs">
</a>

<sub>You can click on the image above to be redirected to the swagger documentation.</sub>


## Features

### Musics
- `Search all musics`: retrieve all musics in the database sorted by artists names and then musics names.
- `Search musics by filter`: search for musics and artists that contains the filter in the name, sorting
by the artists names and then by musics names.

### Playlists
- `Add music to playlist`: add an existing music to an existing playlist.
- `Search musics by playlist`: return all musics in the given playlist.
- `Remove music from playlist`: remove an existing music that is part of playlist from it.

## Contributors
<a href="https://github.com/gruvat/projetct-myMusic-toTal/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=gruvat/projetct-myMusic-toTal" />
</a>
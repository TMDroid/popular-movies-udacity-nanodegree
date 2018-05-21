Popular Movies Udacity Nanodegree

I had to clone `https://github.com/felixsoares/ImageZoom.git` because I have the `MovieDetailsActivity` that uses a `CollapsingToolbarLayout` in which I have an `Image`. The thing is I can't make the `CollapsingToolbarLayout`'s height to fit the whole image so I made it to enlarge on click. the problem was if I clicked on it before it was fully loaded I would get a `NullPointerException` so I had to clone the library and fix it.

Insert your API key in `app/src/main/java/ro/adlabs/popular_movies_nanodegree/util/ApiManager.java`
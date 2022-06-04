const searchBar = document.getElementById("search_bar");
const searchBtn = document.getElementById("search");
const clearBtn = document.getElementById("clear");
const searchInput = document.getElementById("search_input");

var searchContent = document.getElementById("search_results");

var titleContent = document.getElementById("title");
var nationalityContent = document.getElementById("nationality");
var biographyContent = document.getElementById("biography");

var focusCard = document.getElementById("dummy_card");

var active = {};

searchBtn.addEventListener('click', function onClick(event) {
    search();
})

searchInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter") { 
        event.preventDefault();
        searchBtn.click();
    }
});

function search() {
    searchInput.blur();
    var inputText = searchInput.value;
    if (inputText !== "") {
        console.log(inputText)
        clearDetails();
        showLoading();
        hideNoResult();
        ajaxRequest("/search/" + inputText, "search", showSearch);
    }
    return false;
}

function getArtist(artist_id) {
    showLoading();
    clearDetails();
    ajaxRequest("/artist/" + artist_id, "artist", showArtist);
    return false;
}

function showSearch(response) {
    hideLoading();

    let artist_dict = response;
    let length = Object.keys(artist_dict).length;
    if (length === 0) {
        searchContent.setAttribute("style", "margin-top:0px;");
        showNoResult();
        return;
    } else {
        searchContent.setAttribute("style", "margin-top:50px;");
        hideNoResult();
    }

    let artist_cards = "";
    console.log(length);
    let i;
    for (i = 0; i < length; i++) {
        thumbnail_url = artist_dict[i]['thumbnail'];
        artist_name = artist_dict[i]['name'];
        artist_id = artist_dict[i]['artist_id'];
        artist_cards += "<div class=\'artist_card\' id=" + artist_id + ">";
        if (thumbnail_url == "/assets/shared/missing_image.png") {
            artist_cards += "<img class=\'thumbnail\' src=\'artsy_logo.svg\'>";
        } else {
            artist_cards += "<img class=\'thumbnail\' src=\'" + thumbnail_url + "\'>";
        }
        artist_cards += "<p class=\'artist_name\'>" + artist_name + "</p></div>";
    }

    searchContent.innerHTML = artist_cards;
    var cards = document.getElementsByClassName("artist_card");
    let maxHeight = 0;

    for (let card of cards) {
        height = card.offsetHeight;
        if (height > maxHeight) {
            maxHeight = height;
        }
    }
    for (let card of cards) {
        card.addEventListener("click", function() {
            focusCard.style.setProperty("background-color", "#205375");
            card.style.setProperty("background-color", "#122b3c");
            focusCard = card;
            for (var id in active) {
                active[id] = false;
            }
            active[card.id] = true;
            getArtist(card.id);
        });

        card.addEventListener("mouseover", function() {
            card.style.setProperty("background-color", "#122b3c");
        });

        card.addEventListener("mouseleave", function(){
            if (active[card.id] !== true) {
                card.style.setProperty("background-color", "#205375");
            }
        })
        console.log(maxHeight);
        card.setAttribute("style", "height:" + maxHeight + "px;")
    }
}

function showArtist(response) {
    hideLoading();

    artist_name = response['name'];
    birthday = response['birthday'];
    deathday = response['deathday'];
    nationality = response['nationality'];
    biography = response['biography'];

    titleContent.innerHTML = artist_name + " (" + birthday + " - " + deathday + ")";
    nationalityContent.innerHTML = nationality;
    biographyContent.innerHTML = biography;
}

function clearDetails() {
    titleContent.innerHTML = ""
    nationalityContent.innerHTML = ""
    biographyContent.innerHTML = ""
}

function showLoading() {
    var loadingGif = document.getElementById("loading");
    loadingGif.setAttribute("style", "display: block");
}

function hideLoading() {
    var loadingGif = document.getElementById("loading");
    loadingGif.setAttribute("style", "display: none");
}

function showNoResult() {
    searchContent.innerHTML = "<img id=\'loading\' src=\'loading.gif\' style=\'display: none\'></img>";
    searchContent.innerHTML += "<div id=\'no_results\' style=\'display: none;\'>No results found.</div>";
    var noResults = document.getElementById("no_results")
    noResults.setAttribute("style", "display: block;");
}

function hideNoResult() {
    var noResults = document.getElementById("no_results")
    if (noResults !== null) {
        noResults.setAttribute("style", "display: none;");
    }
}

function ajaxRequest(url, type, callBack) {
    let xhr = new XMLHttpRequest();
    console.log(type + " URL: " + url);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(type + " response received");
            callBack(JSON.parse(xhr.responseText));
            console.log(JSON.parse(xhr.responseText));
        } else {
            console.error(xhr.statusText);
        }
    };
    xhr.open("GET", url, true);
    xhr.send();

}

clearBtn.addEventListener('click', function onClick(event) {
    searchInput.value = "";
});

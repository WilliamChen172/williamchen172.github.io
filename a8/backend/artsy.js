const axios = require('axios').default;


module.exports.authenticate = authenticate;
module.exports.search = search;
module.exports.artist = artist;
module.exports.artwork = artwork;
module.exports.genes = genes;

const auth_key = {
    'client_id': 'f1234a6a32bbf387d42e',
    'client_secret': 'ebd85d148400419fd6d9ecc305a91f8d'
}


async function authenticate() {
    try {
        const response = await axios.post('https://api.artsy.net/api/tokens/xapp_token', auth_key);
        const auth_token = response.data.token;
        return auth_token;
    } catch (error) {
        console.error(error);
    }
}

async function search(keyword) {
    try {
        const token = await authenticate();

        params = {'q': keyword, 'size': 10};
        headers = {'X-XAPP-Token': token};
        const response = await axios.get('https://api.artsy.net/api/search', { headers: headers, params: params});

        r = response.data._embedded.results;
        const results = [];
        for (let i in r) {
            if (r[i].og_type == "artist") {
                artist_link = r[i]._links.self.href;
                id = artist_link.split('/').pop();
                artist = {name: r[i].title, thumbnail: r[i]._links.thumbnail.href, id: id};
                results.push(artist);
            }
        }
        return results;
    } catch (error) {
        console.error(error);
    }
}

async function artist(artist_id) {
    try {
        const token = await authenticate();
        headers = {'X-XAPP-Token': token};
        const response = await axios.get('https://api.artsy.net/api/artists/' + artist_id, { headers: headers});
        r = response.data;
        artist = {name: r.name, birthday: r.birthday, deathday: r.deathday, nationality: r.nationality, biography: r.biography};
        return artist;
    } catch (error) {
        console.error(error);
    }
}

async function artwork(artist_id) {
    try {
        const token = await authenticate();
        params = {artist_id: artist_id, size: 10};
        headers = {'X-XAPP-Token': token};
        const response = await axios.get('https://api.artsy.net/api/artworks', { headers: headers, params: params});

        a = response.data._embedded.artworks;
        const artworks = [];
        for (let i in a) {
            artwork = {id: a[i].id, title: a[i].title, date: a[i].date, thumbnail: a[i]._links.thumbnail.href};
            artworks.push(artwork);
        }
        return artworks;
    } catch (error) {
        console.error(error);
    }
}

async function genes(artwork_id) {
    try {
        const token = await authenticate();
        params = {artwork_id: artwork_id};
        headers = {'X-XAPP-Token': token};
        const response = await axios.get('https://api.artsy.net/api/genes', { headers: headers, params: params});

        g = response.data._embedded.genes;
        const genes = [];
        for (let i in g) {
            gene = {name: g[i].name, thumbnail: g[i]._links.thumbnail.href};
            genes.push(gene);
        }
        return genes;
    } catch (error) {
        console.error(error);
    }
}

const express = require('express');
const app = express();
const artsy = require('./artsy');

app.use(express.static("../frontend/dist/my-app"))

app.get('/', (req, res) => {
  res.sendFile('index.html', {root: __dirname})
});

// Listen to the App Engine-specified port, or 8080 otherwise
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}...`);
});

app.get('/search/:keyword', async function (req, res) {
    console.log(`\nSearch: ${req.params.keyword}`);
    let response = await artsy.search(req.params.keyword);
    let msg = `Search call finished at ${Date()}\nLength of response: ${response.length}`;
    console.log(msg);
    return res.send(response);
})

app.get('/artist/:artist_id', async function (req, res) {
    let response = await artsy.artist(req.params.artist_id);
    console.log(`Artist call finished at ${Date()}\n`);
    return res.send(response);
})

app.get('/artwork/:artist_id', async function (req, res) {
    let response = await artsy.artwork(req.params.artist_id);
    console.log(`Artwork call finished at ${Date()}\n`);
    return res.send(response);
})

app.get('/genes/:artwork_id', async function (req, res) {
    let response = await artsy.genes(req.params.artwork_id);
    console.log(`Genes call finished at ${Date()}\n`);
    return res.send(response);
})

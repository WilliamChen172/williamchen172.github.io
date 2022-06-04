from flask import Flask, request, redirect, url_for, jsonify
import requests
from process import *

app = Flask(__name__)

auth = {
    'client_id': 'f1234a6a32bbf387d42e',
    'client_secret': 'ebd85d148400419fd6d9ecc305a91f8d'
}
auth_token = ""
auth_timestamp = ""


@app.route('/')
def home():
    return redirect(url_for('static', filename='home.html'))


def authenticate():
    global auth_token, auth_timestamp
    r = requests.post('https://api.artsy.net/api/tokens/xapp_token', data=auth)
    auth_token = r.json()['token']
    auth_timestamp = r.json()['expires_at']


@app.route('/search/<string:search_term>')
def search(search_term):
    authenticate()
    params = {'q': search_term, 'size': 10}
    headers = {'X-XAPP-Token': auth_token}
    r = requests.get('https://api.artsy.net/api/search', headers=headers, params=params)
    r = r.json()['_embedded']['results']
    results = jsonify(p_search(r))
    return results


@app.route('/artist/<string:artist_id>')
def artist(artist_id):
    authenticate()
    headers = {'X-XAPP-Token': auth_token}
    print('https://api.artsy.net/api/artists/' + artist_id)
    r = requests.get('https://api.artsy.net/api/artists/' + artist_id, headers=headers)
    a = r.json()
    details = jsonify(p_artist(a))
    return details


if __name__ == '__main__':
    # This is used when running locally only. When deploying to Google App
    # Engine, a webserver process such as Gunicorn will serve the app. You
    # can configure startup instructions by adding `entrypoint` to app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)


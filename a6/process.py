def p_search(r):
    results = []
    for result in r:
        if result['og_type'] == 'artist':
            artist = {'name': result['title'], 'thumbnail': result['_links']['thumbnail']['href']}
            artist_link = result['_links']['self']['href']
            artist['artist_id'] = artist_link.split('/')[-1]
            results.append(artist)
    return results


def p_artist(a):
    artist = {'name': a['name'],
              'birthday': a['birthday'],
              'deathday': a['deathday'],
              'nationality': a['nationality'],
              'biography': a['biography']}
    return artist







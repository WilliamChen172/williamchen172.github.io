import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Artwork } from './artwork';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class ArtworkService {
  private artworkURL = "/artwork/"

  constructor(private http: HttpClient) { }

  getArtworks(artist_id: string): Observable<Artwork[]> {
    console.log(this.artworkURL + artist_id);
    return this.http.get<Artwork[]>(this.artworkURL + artist_id)
  }
}

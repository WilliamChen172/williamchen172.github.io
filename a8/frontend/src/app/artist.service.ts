import { Injectable } from '@angular/core';
import { Artist_info } from './artist-info';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {
  private artistURL = "/artist/"
  
  constructor(private http: HttpClient) { }

  getArtist(artist_id: string): Observable<Artist_info> {
    console.log(this.artistURL + artist_id);
    return this.http.get<Artist_info>(this.artistURL + artist_id)
  }
}

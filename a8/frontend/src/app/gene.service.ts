import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Gene } from './gene';

@Injectable({
  providedIn: 'root'
})
export class GeneService {
  private geneURL = "/genes/"

  constructor(private http: HttpClient) { }

  getArtworkGenes(artwork_id: string): Observable<Gene[]> {
    console.log(this.geneURL + artwork_id);
    return this.http.get<Gene[]>(this.geneURL + artwork_id);
  }
}

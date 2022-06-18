import { Injectable } from '@angular/core';
import { Artist } from './artist';
import { Observable, catchError, tap, of} from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private searchURL = "/search/";

  constructor(private http: HttpClient) { }

  getSearchResults(search_form: string): Observable<Artist[]> {
    console.log(this.searchURL + search_form);
    return this.http.get<Artist[]>(this.searchURL + search_form);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}

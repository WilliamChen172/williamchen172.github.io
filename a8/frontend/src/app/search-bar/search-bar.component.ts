import { Component, Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Artist } from '../artist';
import { SearchService } from '../search.service';
import { DisplayService } from '../display.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})

@Injectable()
export class SearchBarComponent implements OnInit {

  private searchURL = "/search/";
  isDisabled: boolean = true;
  isSearching: boolean = false;
  isCleared: boolean = false;
  noResults: boolean = false;

  search_form = "";
  artists: Artist[] = [];
  display_detail = undefined

  constructor(private searchService: SearchService, private displayService: DisplayService) { }

  enterSearchState() {
    this.isSearching = true
  }

  exitSearchState() {
    this.isSearching = false
    this.isCleared = false
    this.displayService.hideDisplay();
  }

  showNoResults() {
    this.noResults = true;
  }

  hideNoResults() {
    this.noResults = false;
  }

  getSearchResults(): void {
    this.enterSearchState();
    this.searchService.getSearchResults(this.search_form).subscribe(results => {
      this.artists = results;
      this.exitSearchState();
      if (this.artists.length === 0) {
        this.showNoResults();
      } else {
        this.hideNoResults();
        for (let i in this.artists) {
          if (this.artists[i].thumbnail === "/assets/shared/missing_image.png") {
            this.artists[i].thumbnail = "assets/artsy_logo.svg";
          }
        }
      }

    });
  }

  clearSearch() {
    this.search_form = ""
    this.isCleared = true;
    this.setDisabled();
  }

  setDisabled() {
    if (this.search_form == "") {
      this.isDisabled = true;
    } else {
      this.isDisabled = false;
    }
  }

  onChange(event: any) {
    this.search_form = event;
    this.setDisabled();
  }

  ngOnInit(): void {
  }

}

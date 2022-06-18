import { Component, OnInit, Input } from '@angular/core';
import { Artist_info } from '../artist-info';
import { Artwork } from '../artwork';
import { DisplayService } from '../display.service';

@Component({
  selector: 'app-artist-detail',
  templateUrl: './artist-detail.component.html',
  styleUrls: ['./artist-detail.component.css']
})
export class ArtistDetailComponent implements OnInit {
  @Input() artist_info!: Artist_info;
  @Input() artworks!: Artwork[];
  isInfo: boolean = true;
  shouldHide: boolean = false;
  noResults: boolean = false;

  constructor(private displayService: DisplayService) { 
    this.displayService.hide.subscribe(shouldHide => this.shouldHide = shouldHide)
  }

  showNoResults() {
    this.noResults = true;
  }

  hideNoResults() {
    this.noResults = false;
  }

  switchTab() {
    this.isInfo = !this.isInfo
    if (this.artworks.length === 0 && !this.isInfo) {
      this.showNoResults()
    } else {
      this.hideNoResults()
    }
  }



  ngOnInit(): void {
  }

}

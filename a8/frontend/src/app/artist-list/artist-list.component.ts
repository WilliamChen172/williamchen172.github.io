import { Component, OnInit, Input } from '@angular/core';
import { ArtistService } from '../artist.service';
import { ArtworkService } from '../artwork.service';
import { Artist } from '../artist';
import { Artist_info } from '../artist-info';
import { Artwork } from '../artwork';


@Component({
  selector: 'app-artist-list',
  templateUrl: './artist-list.component.html',
  styleUrls: ['./artist-list.component.css']
})
export class ArtistListComponent implements OnInit {
  constructor(private artistService: ArtistService, private artworkService: ArtworkService) {
  }

  @Input() artists?: Artist[]
  artist_info: Artist_info = {} as Artist_info
  artworks: Artwork[] = []
  selected_artist?: Artist;
  isSearching: boolean = false;

  card_style: string = "min-width: 230px; width: 230px; border-style: none; margin-right: 2px; border-radius: 2px;"
  light_color: string = "background-color: #205375"
  dark_color: string = "background-color: #122b3c"

  mouseOverArtist?: Artist;
  mouseOver?: boolean = false;

  enterSearchState() {
    this.isSearching = true
  }

  exitSearchState() {
    this.isSearching = false
  }

  getArtistDetails(artist_id: string): void {
    this.artistService.getArtist(artist_id).subscribe(artist_info => {
      this.artist_info = artist_info
    });
  }

  getArtworks(artist_id: string) {
    this.artworkService.getArtworks(artist_id).subscribe(artworks => {
      this.artworks = artworks
      this.exitSearchState();
    })
  }

  onArtistClick(artist: Artist) {
    this.enterSearchState();
    this.selected_artist = artist;
    this.getArtistDetails(this.selected_artist.id);
    this.getArtworks(this.selected_artist.id)
  }

  onMouseOver(artist: Artist) {
    this.mouseOver = true
    this.mouseOverArtist = artist
  }

  onMouseLeave() {
    this.mouseOver = false
  }

  dynamic_card_style(artist: Artist) {
    if (artist === this.selected_artist) {
      return this.card_style + this.dark_color
    } else if (artist === this.mouseOverArtist && this.mouseOver) {
      return this.card_style + this.dark_color
    } else {
      return this.card_style + this.light_color
    }
  }

  ngOnInit(): void {
  }


}

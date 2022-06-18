import { Component, OnInit, Input } from '@angular/core';
import { Artwork } from '../artwork';
import { Gene } from '../gene';
import { GeneService } from '../gene.service';

@Component({
  selector: 'app-artworks',
  templateUrl: './artworks.component.html',
  styleUrls: ['./artworks.component.css']
})

export class ArtworksComponent implements OnInit {
  @Input() artworks?: Artwork[]
  genes: Gene[] = []
  selected_artwork: Artwork = {} as Artwork
  isSearching: boolean = false;
  noResults: boolean = false;

  constructor(private geneService: GeneService) {  }

  enterSearchState() {
    this.isSearching = true
  }

  exitSearchState() {
    this.isSearching = false
  }

  showNoResults() {
    this.noResults = true;
  }

  hideNoResults() {
    this.noResults = false;
  }

  selectArtwork(artwork: Artwork) {
    this.selected_artwork = artwork
    this.getArtworkGenes(artwork.id)
  }

  getArtworkGenes(artwork_id: string) {
    this.enterSearchState();
    this.geneService.getArtworkGenes(artwork_id).subscribe(genes => {
      this.genes = genes
      this.exitSearchState();
      if (this.genes.length === 0) {
        this.showNoResults();
      } else {
        this.hideNoResults();
      }
    })
  }

  ngOnInit(): void {
  }

}

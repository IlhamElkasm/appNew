import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../Model/environment'; 

@Injectable({
  providedIn: 'root'
})
export class CloudinaryService {
  private cloudinaryUrl = `https://api.cloudinary.com/v1_1/${environment.cloudinary.cloudName}/image/upload`;
  private uploadPreset = environment.cloudinary.uploadPreset;
  
  constructor(private http: HttpClient) { }
  
  uploadImage(image: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', image);
    formData.append('upload_preset', this.uploadPreset); 
    return this.http.post(
      this.cloudinaryUrl,
      formData
    );
  }
  
  
}
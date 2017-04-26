# Image Painter
在图片上画画或签名

Paint or sign on image


![image](https://github.com/Beenlong/ImagePainter/blob/master/pic/preview.gif)


## Dependencies

```
compile 'net.zombie_sama.imagepainter:imagepainter:0.1.2'
```


## Usage

Simply put `net.zombie_sama.imagepainter.PaintableImageView` in your layout just like a normal `ImageView`

```java
//Set image
PaintableImageView.setImageBitmap(Bitmap bitmap);

//Get current bitmap
PaintableImageView.getResult();

//Revert modification
PaintableImageView.reset();

//Custom paint
PaintableImageView.setPaint(Paint paint);
```
package at.ac.htlhl.nucleij.presenter.converter;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import ij.IJ;

/**
 * Created by andreas on 28.12.16.
 */

public class MainConverter{
    private NdpiConverter ndpiConverter;

    public MainConverter(NdpiConverter ndpiConverter) {
        //super(ndpiConverter);
        this.ndpiConverter = ndpiConverter;
    }

    public String startConverter(String filePath) {
        String magnification = ndpiConverter.getMagnification();
        //String directory = ndpiConverter.getInputpath();
        String command = ("no=[%]").replace("%", filePath);
        command += " format_of_split_images=[TIFF with LZW compression] make_mosaic=never mosaic_pieces_format=[TIFF with JPEG compression] requested_jpeg_compression=75 mosaic_pieces_overlap=0.000000 mosaic_pieces_overlap_unit=pixels size_limit_on_each_mosaic_piece=1024 width_of_each_mosaic_piece_in_pixels=0 height_of_each_mosaic_piece_in_pixels=0 ";
        magnification = "extract_images_at_magnification_%".replace("%", magnification);
        command = command + magnification + " extract_images_with_z-offset_0";

        IJ.run("Custom extract to TIFF / Mosaic...", command);

        return filePath.replace(".ndpi", "_".concat(magnification).concat("_z0.tif"));
    }
}

package net.cyclestreets;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewPathOverlay;
import org.andnav.osm.views.util.OpenStreetMapRendererInfo;

import uk.org.invisibility.cycloid.CycloidConstants;
import uk.org.invisibility.cycloid.CycloidResourceProxy;
import uk.org.invisibility.cycloid.MapActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class PhotomapActivity extends Activity implements CycloidConstants {
	protected PhotomapListener photomapListener;
	protected DispatchingMapView photomapView;
	
	private ScrollListenerMapView map; 
	private OpenStreetMapViewPathOverlay path;
	private MyLocationOverlay location;
	private OpenStreetMapViewItemizedOverlay<PhotoItem> markers;
	protected List<PhotoItem> photoList;
	private ResourceProxy proxy;
	private SharedPreferences prefs;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        proxy = new CycloidResourceProxy(getApplicationContext());
        prefs = getSharedPreferences(PREFS_APP_KEY, MODE_PRIVATE);
        photoList = new CopyOnWriteArrayList<PhotoItem>();

        map = new ScrollListenerMapView
        (
    		this,
    		OpenStreetMapRendererInfo.values()[prefs.getInt(PREFS_APP_RENDERER, MAPTYPE.ordinal())],
    		MapActivity.map
        );
        map.setResourceProxy(proxy);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getController().setZoom(prefs.getInt(PREFS_APP_ZOOM_LEVEL, 14));
        map.scrollTo(prefs.getInt(PREFS_APP_SCROLL_X, 0), prefs.getInt(PREFS_APP_SCROLL_Y, -701896)); /* Greenwich */
        photoList.add(new PhotoItem("test", "description", map.getMapCenter()));
        map.addScrollListener(new PhotomapListener(map, photoList));

        markers = new OpenStreetMapViewItemizedOverlay<PhotoItem>(this, photoList,
        		new OpenStreetMapViewItemizedOverlay.OnItemTapListener<PhotoItem>() {
					public boolean onItemTap(int index, PhotoItem photo) {
						Toast.makeText(PhotomapActivity.this, photo.mDescription, Toast.LENGTH_SHORT).show();
						// TODO act on tap
						return false;
					}
        });
        map.getOverlays().add(markers);
        
        final RelativeLayout rl = new RelativeLayout(this);
        rl.addView(this.map, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.setContentView(rl);
        
//        // create photomap
//        photomapView = new DispatchingMapView(this, CycleStreets.mapComponent);
//    	photomapListener = new PhotomapListener();
//        Log.d(getClass().getSimpleName(), "before: " + CycleStreets.mapComponent.getOnMapElementListener());
//    	// create listener for clicks on photos
//        CycleStreets.mapComponent.setOnMapElementListener(this);
//        Log.d(getClass().getSimpleName(), "after: " + CycleStreets.mapComponent.getOnMapElementListener());
//
//        // create ZoomControls
//        ZoomControls zoomControls = new ZoomControls(this);
//        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
//        	public void onClick(final View v) {
//        		CycleStreets.mapComponent.zoomIn();
//        	}
//        });
//        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
//        	public void onClick(final View v) {
//        		CycleStreets.mapComponent.zoomOut();
//        	}
//        });
//
//        RelativeLayout photomapLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams mapViewLayoutParams = new RelativeLayout.LayoutParams(
//        		RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//        photomapLayout.addView(photomapView, mapViewLayoutParams);
//    	setContentView(photomapLayout);
//
//        // add Zoom controls to the RelativeLayout
//        RelativeLayout.LayoutParams zoomControlsLayoutParams = new RelativeLayout.LayoutParams(
//        		RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        zoomControlsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        zoomControlsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        photomapLayout.addView(zoomControls, zoomControlsLayoutParams);  
//    	
//    	Toast.makeText(this, "fetching photos", Toast.LENGTH_LONG).show();
//    	try {
//        	WgsPoint center = CycleStreets.CAMBRIDGE;
//        	int zoom = 7;
//            double w=-5.8864316;
//            double s=50.1920909;
//            double e=4.3967716;
//            double n=54.2277333;
//        	
//        	List<Photo> photos = CycleStreets.apiClient.getPhotos(center, zoom, n, s, e, w);
//        	Toast.makeText(this, photos.get(0).caption, Toast.LENGTH_LONG).show();
//        }
//        catch (Exception e) {
//        	throw new RuntimeException(e);
//        }
    }

//	/* (non-Javadoc)
//	 * @see android.app.Activity#onPause()
//	 */
//	@Override
//	protected void onPause() {
//		super.onPause();
//
//		photomapView.removeMapListener(photomapListener);
//	}
//
//	/* (non-Javadoc)
//	 * @see android.app.Activity#onResume()
//	 */
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		// add listener to dynamically add photos as map is moved
//	    photomapView.addMapListener(photomapListener);
//	    
//	    // force initial refresh
//	    photomapListener.mapMoved();
//	}
//
//    @Override
//    protected void onDestroy() {
//  	  super.onDestroy();
//  	  if (photomapView != null) {
//  	      photomapView.clean();
//  	      photomapView = null;
//  	    }
//  	}

//	@Override
//	public void elementClicked(OnMapElement arg0) {
//		Log.d(getClass().getSimpleName(), "elementClicked " + ((Place) arg0).getLabel());
//		String url = photomapListener.photoMap.get(((Place) arg0).getId()).thumbnailUrl;
//		Log.d(getClass().getSimpleName(), "URL is " + url);
//		if (arg0 instanceof Place) {
//			startActivity(new Intent(Intent.ACTION_VIEW,
//					Uri.parse(url),
//					this, DisplayPhotoActivity.class));
//		}
//	}
//
//	@Override
//	public void elementEntered(OnMapElement arg0) {
//		// TODO: show tool tip
//	}
//
//	@Override
//	public void elementLeft(OnMapElement arg0) {
//		// TODO: hide tool tip
//	}
}

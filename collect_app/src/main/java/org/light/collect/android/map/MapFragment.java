/*
 * Copyright (C) 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.light.collect.android.map;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.List;

/**
 * Interface for a Fragment that renders a map view.  The plan is to have one
 * implementation for each map SDK, e.g. GoogleMapFragment, OsmMapFragment, etc.
 *
 * This is intended to be a single map API that provides all functionality needed
 * for the three geo widgets (collecting or editing a point, a trace, or a shape):
 *   - Basic control of the viewport (panning, zooming)
 *   - Displaying and getting the current GPS location
 *   - Requesting a callback on the first GPS location fix
 *   - Requesting callbacks for short clicks and long presses on the map
 *   - (to do) Adding editable points to the map
 *   - (to do) Adding editable traces (polylines) to the map
 *   - Adding editable shapes (closed polygons) to the map
 *
 * Editable points, traces, and shapes are called "map features" in this API.
 * To keep the API small, features are not exposed as objects; instead, they are
 * identified by integer feature IDs.  To keep the API unified (instead of having
 * three distinct modes), the map always supports all three kinds of features,
 * even though the geo widgets only use one kind of feature at a time.
 */
public interface MapFragment {
    /**
     * Adds the map Fragment to an activity.  The containerId should be the
     * resource ID of a View, into which the map view will be placed.  The
     * listener will be invoked on the UI thread, with this MapFragment when the
     * map is ready, or with null if there is a problem initializing the map.
     */
    void addTo(@NonNull FragmentActivity activity, int containerId, @Nullable ReadyListener listener);

    /** Gets the point currently shown at the center of the map view. */
    @NonNull MapPoint getCenter();

    /**
     * Gets the current zoom level.  For maps that only support zooming by
     * powers of 2, the zoom level will always be an integer.
     */
    double getZoom();

    /** Centers the map view on the given point, leaving zoom level unchanged. */
    void setCenter(@Nullable MapPoint center);

    /**
     * Centers the map view on the given point, zooming in to a close-up level
     * deemed appropriate by the implementation, possibly with animation.
     */
    void zoomToPoint(@Nullable MapPoint center);

    /**
     * Centers the map view on the given point with a zoom level as close as
     * possible to the given zoom level, possibly with animation.
     */
    void zoomToPoint(@Nullable MapPoint center, double zoom);

    /**
     * Adjusts the map's viewport to enclose all of the given points, possibly
     * with animation.  A scaleFactor of 1.0 ensures that all the points will be
     * just visible in the viewport; a scaleFactor less than 1 shrinks the view
     * beyond that.  For example, a scaleFactor of 0.8 causes the bounding box
     * to occupy at most 80% of the width and 80% of the height of the viewport,
     * ensuring a margin of at least 10% on all sides.
     */
    void zoomToBoundingBox(Iterable<MapPoint> points, double scaleFactor);

    /**
     * Adds a polyline or polygon to the map with the given sequence of vertices.
     * The vertices will have handles that can be dragged by the user.
     * Returns a positive integer, the featureId for the newly added shape.
     */
    int addDraggablePoly(@NonNull Iterable<MapPoint> points, boolean closedPolygon);

    /** Appends a vertex to the polyline or polygon specified by featureId. */
    void appendPointToPoly(int featureId, @NonNull MapPoint point);

    /**
     * Returns the vertices of the polyline or polygon specified by featureId, or an
     * empty list if the featureId does not identify an existing polyline or polygon.
     */
    @NonNull List<MapPoint> getPointsOfPoly(int featureId);

    /** Removes a specified map feature from the map. */
    void removeFeature(int featureId);

    /** Removes all map features from the map. */
    void clearFeatures();

    /**
     * Enables/disables GPS tracking.  While enabled, the GPS location is shown
     * on the map, the first GPS fix will trigger any pending callbacks set by
     * runOnGpsLocationReady(), and every GPS fix will invoke the callback set
     * by setGpsLocationListener().
     */
    void setGpsLocationEnabled(boolean enabled);

    /** Gets the last GPS location fix, or null if there hasn't been one. */
    @Nullable MapPoint getGpsLocation();

    /**
     * Queues a callback to be invoked on the UI thread as soon as a GPS fix is
     * available.  If there already is a location fix, the callback is invoked
     * immediately; otherwise, when a fix is obtained, it will be invoked once.
     * To begin searching for a GPS fix, call setGpsLocationEnabled(true).
     * Activities that set callbacks should call setGpsLocationEnabled(false)
     * in their onStop() or onDestroy() methods, to prevent invalid callbacks.
     */
    void runOnGpsLocationReady(@NonNull ReadyListener listener);

    /**
     * Sets or clears the callback for GPS location updates.  This callback
     * will only be invoked while GPS is enabled with setGpsLocationEnabled().
     */
    void setGpsLocationListener(@Nullable PointListener listener);

    /** Sets or clears the callback for a click on the map. */
    void setClickListener(@Nullable PointListener listener);

    /** Sets or clears the callback for a long press on the map. */
    void setLongPressListener(@Nullable PointListener listener);

    interface ReadyListener {
        void onReady(@Nullable MapFragment mapFragment);
    }

    interface PointListener {
        void onPoint(@NonNull MapPoint point);
    }
}

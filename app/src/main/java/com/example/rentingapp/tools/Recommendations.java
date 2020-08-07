package com.example.rentingapp.tools;

import com.example.rentingapp.adapters.ListingsAdapter;
import com.example.rentingapp.models.Listing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recommendations {
    List<Listing> listings;
    ListingsAdapter adapter;

    HashMap<Listing, List<String>> listingsToUsers = new HashMap<>();
    HashMap<String, Set<String>> usersToListings = new HashMap<>();

    public Recommendations(List<Listing> listings, ListingsAdapter adapter) {
        this.listings = listings;
        this.adapter = adapter;
    }

    public void driver(final String userID) {
        // LISTINGS TO USERS
        for (Listing listing : listings) {
            // must make sure that likes are not null
            List<String> likedBy = listing.getLikedBy();
            if (likedBy == null) {
                likedBy = new ArrayList<>();
            }

            listingsToUsers.put(listing, likedBy);
        }

        // would also need to change users to if (likes == null && !likes.isEmpty() && !user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId))
//        Iterator<Listing> listingIterator = listings.iterator();
//        while (listingIterator.hasNext()) {
//            Listing listing = listingIterator.next();
//            List<String> likedBy = listing.getLikedBy();
//            if (likedBy != null && !likedBy.isEmpty() && !likedBy.contains(ParseUser.getCurrentUser().getObjectId())) {
//                listingsToUsers.put(listing, likedBy);
//                listingIterator.remove();
//            }
//        }


        // USERS TO LISTINGS
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        queryUsers.include("likes");
        queryUsers.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                for (ParseUser user: users) {
                    List<String> likes = user.getList("likes");
                    if (likes == null) {
                        likes = new ArrayList<>();
                    }

                    usersToListings.put(user.getObjectId(), new HashSet<>(likes));
                }

                List<Listing> recommendedOrderListings = getRecommendations(userID);
                listings = recommendedOrderListings;
                adapter.setListings(recommendedOrderListings);
            }
        });
    }

    public double getUserSimilarity(String user1ID, String user2ID) {
        Set<String> user1Listings = usersToListings.get(user1ID);
        Set<String> user2Listings = usersToListings.get(user2ID);

        Set<String> intersection = new HashSet<>(user1Listings);
        intersection.retainAll(user2Listings);

        if (intersection.size() == 0) {
            return 0.0;
        }

        int unionSize = user1Listings.size() + user2Listings.size() - intersection.size();

        return (double) intersection.size() / (double) unionSize;
    }

    public double getListingScore(String userID, Listing listing) {
        List<String> usersWhoLikeListing = listingsToUsers.get(listing);

        double totalSimilarity = 0.0;
        for (String iterUserID : usersWhoLikeListing) {
            totalSimilarity += getUserSimilarity(userID, iterUserID);
        }
        return totalSimilarity / (double) usersWhoLikeListing.size();
    }

    public List<Listing> getRecommendations(String userID) {
        Comparator<Listing> comparator = new Comparator<Listing>() {
            @Override
            public int compare(Listing l1, Listing l2) {
                int compareValue = l1.getScore().compareTo(l2.getScore());
                if (compareValue > 0) { // TODO: replace with Integer.compare
                    return -1;
                } else if (compareValue < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        List<Listing> recommendedOrderListings = new ArrayList<>();
        Set<String> callingUsersListings = usersToListings.get(userID);

        for (Listing listing : listingsToUsers.keySet()) {
            // Only recommend listings that aren't the user's and the user hasn't already saved
            if (!listing.getSeller().getObjectId().equals(userID) && !callingUsersListings.contains(listing.getObjectId())) {
                double score = getListingScore(userID, listing);
                if (Double.isNaN(score) || score == (double) 0.0) {
                    // If there is no recommendation data for the listing, give it a sorting score based on its creation date
                    score = (double) -1 / (double) listing.getCreatedAt().getTime();
                }
                listing.setScore(score);
            } else {
                // If the listing belongs to the user or has already been saved, give it a sorting score based on its creation date
                listing.setScore((double) -1 / (double) listing.getCreatedAt().getTime());
            }
            recommendedOrderListings.add(listing);
        }

        // Sort the listings to show recommended listings at the top, while maintaining data order for all others
        Collections.sort(recommendedOrderListings, comparator);
        return recommendedOrderListings;
    }


//    ----------------------------------------------------------------------------------------------------


//    public void loadTestData() {
//        Set<String> UserASet = new HashSet<>();
//        UserASet.add("a");
//        UserASet.add("c");
//        UserASet.add("d");
//        UserASet.add("e");
//        UserASet.add("f");
//        usersToListings.put("a", UserASet);
//
//        Set<String> UserBSet = new HashSet<>();
//        UserBSet.add("a");
//        UserBSet.add("b");
//        usersToListings.put("b", UserBSet);
//
//        Set<String> UserCSet = new HashSet<>();
//        UserCSet.add("a");
//        UserCSet.add("c");
//        UserCSet.add("d");
//        UserCSet.add("e");
//        UserCSet.add("f");
//        usersToListings.put("c", UserCSet);
//
//        Set<String> UserXSet = new HashSet<>();
//        UserXSet.add("a");
//        UserXSet.add("b");
//        UserXSet.add("c");
//        UserXSet.add("f");
//        usersToListings.put("x", UserXSet);
//
//        Set<String> usersWhoLikeA = new HashSet<>();
//        usersWhoLikeA.add("a");
//        usersWhoLikeA.add("b");
//        usersWhoLikeA.add("c");
//        usersWhoLikeA.add("x");
//        listingsToUsers.put("a", usersWhoLikeA);
//
//        Set<String> usersWhoLikeB = new HashSet<>();
//        usersWhoLikeB.add("b");
//        usersWhoLikeB.add("x");
//        listingsToUsers.put("b", usersWhoLikeB);
//
//        Set<String> usersWhoLikeC = new HashSet<>();
//        usersWhoLikeC.add("a");
//        usersWhoLikeC.add("c");
//        usersWhoLikeC.add("x");
//        listingsToUsers.put("c", usersWhoLikeC);
//
//        Set<String> usersWhoLikeD = new HashSet<>();
//        usersWhoLikeD.add("a");
//        usersWhoLikeD.add("c");
//        listingsToUsers.put("d", usersWhoLikeD);
//
//        Set<String> usersWhoLikeE = new HashSet<>();
//        usersWhoLikeE.add("a");
//        usersWhoLikeE.add("c");
//        listingsToUsers.put("e", usersWhoLikeE);
//
//        Set<String> usersWhoLikeF = new HashSet<>();
//        usersWhoLikeF.add("a");
//        usersWhoLikeF.add("c");
//        usersWhoLikeF.add("x");
//        listingsToUsers.put("f", usersWhoLikeF);
//    }





//    ----------------------------------------------------------------------------------------------------

    public Set<String> getUserNeighborhood(String userID) {
        return getUserNeighborhood(userID, 0.01);
    }

    public Set<String> getUserNeighborhood(String userID, double threshold) {
        Set<String> allUsersSet = usersToListings.keySet();
        Set<String> neighbors = new HashSet<>();

        for (String iterUserID : allUsersSet) {
            if (!userID.equals(iterUserID)) {
                double userSimilarity = getUserSimilarity(userID, iterUserID);
                if (!Double.isNaN(userSimilarity) && userSimilarity >= threshold) {
                    neighbors.add(iterUserID);
                }
            }
        }

        // calling function must check if num neighbors == 0
        return neighbors;
    }
}
